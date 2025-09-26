package its.fullstack.biblioteca_proj.controller;

import its.fullstack.biblioteca_proj.dto.PrestitoDTO;
import its.fullstack.biblioteca_proj.exception.*;
import its.fullstack.biblioteca_proj.models.Prestito;
import its.fullstack.biblioteca_proj.models.User;
import its.fullstack.biblioteca_proj.service.PrestitoService;
import its.fullstack.biblioteca_proj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prestiti")
public class PrestitoController {

    @Autowired
    private PrestitoService prestitoService;
    @Autowired
    private UserService userService;

    // Metodo che ottiene l'ID dell'utente corrente
    private Long getCurrentUserId(Authentication auth) {
        String email = auth.getName();
        User currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException((String) null));
        return currentUser.getId();
    }

    // Metodo helper per verificare i permessi
    private void checkUserPermission(Authentication auth, Long targetUserId) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return; // L'admin può fare tutto
        }
        Long currentUserId = getCurrentUserId(auth);
        if (!currentUserId.equals(targetUserId)) {
            throw new AccessDeniedException("Non hai i permessi per eseguire questa operazione per un altro utente.");
        }
    }

    // Metodo per creare un nuovo prestito
    @PostMapping("/nuovo")
    public ResponseEntity<PrestitoDTO> creaPrestito(@Valid @RequestBody PrestitoDTO requestDto) {
        // La logica di controllo dei permessi è già perfetta!
        checkUserPermission(SecurityContextHolder.getContext().getAuthentication(), requestDto.getUserId());
        Prestito prestito = prestitoService.creaPrestito(requestDto.getUserId(), requestDto.getBookId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new PrestitoDTO(prestito));
    }

    // Metodo per restituire un libro
    @PostMapping("/restituisci")
    public ResponseEntity<PrestitoDTO> restituisciLibro(@Valid @RequestBody PrestitoDTO requestDto) {
        checkUserPermission(SecurityContextHolder.getContext().getAuthentication(), requestDto.getUserId());
        Prestito prestitoRestituito = prestitoService.restituisciLibro(requestDto.getUserId(), requestDto.getBookId());
        return ResponseEntity.ok(new PrestitoDTO(prestitoRestituito));
    }


    // Metodo per ottenere tutti i prestiti di un utente
    @GetMapping("/utente/{userId}")
    public ResponseEntity<List<PrestitoDTO>> getPrestitiUtente(@PathVariable Long userId) {
        checkUserPermission(SecurityContextHolder.getContext().getAuthentication(), userId);
        List<Prestito> prestiti = prestitoService.getPrestitiByUserId(userId);
        List<PrestitoDTO> prestitiDTO = prestiti.stream()
                .map(PrestitoDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(prestitiDTO);
    }

    // Metodo per ottenere tutti i prestiti attivi di un utente
    @GetMapping("/attivi/utente/{userId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<List<PrestitoDTO>> getPrestitiAttiviUtente(@PathVariable Long userId) {
        checkUserPermission(SecurityContextHolder.getContext().getAuthentication(), userId);
        List<Prestito> prestiti = prestitoService.getPrestitiUtenteAttivi(userId);
        List<PrestitoDTO> prestitiDTO = prestiti.stream()
                .map(PrestitoDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(prestitiDTO);
    }

    // Metodo per ottenere tutti i prestiti fatti da tutti gli utenti [SOLO ADMIN]
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PrestitoDTO>> getAllPrestiti() {
        List<Prestito> prestiti = prestitoService.getAllPrestiti();
        List<PrestitoDTO> prestitiDTO = prestiti.stream()
                .map(PrestitoDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(prestitiDTO);
    }

    // === GESTIONE ECCEZIONI SPECIFICHE DEL CONTROLLER ===
    @ExceptionHandler({UserNotFoundException.class, BookNotFoundException.class, PrestitoNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }//Messaggio di errore se l'utente non esiste

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<Map<String, String>> handleConflict(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleForbidden(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", ex.getMessage()));
    }
}