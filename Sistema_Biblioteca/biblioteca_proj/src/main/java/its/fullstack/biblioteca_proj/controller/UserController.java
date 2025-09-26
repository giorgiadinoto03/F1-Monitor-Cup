package its.fullstack.biblioteca_proj.controller;

import its.fullstack.biblioteca_proj.dto.UserDto;
import its.fullstack.biblioteca_proj.dto.UserResponseDto; // <-- IMPORTA IL NUOVO DTO
import its.fullstack.biblioteca_proj.exception.EmailAlreadyUsedException;
import its.fullstack.biblioteca_proj.exception.UserNotFoundException;
import its.fullstack.biblioteca_proj.models.User;
import its.fullstack.biblioteca_proj.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    // @PreAuthorize("hasRole('ADMIN')") // Nota: Questo controllo non è necessario, anche gli utenti possono vedere la lista di altri utenti.
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserResponseDto> userDtos = users.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    // Restituisce i dati di un singolo utente (senza dati sensibili come la password).
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        checkUserPermission(id); // Controlla se l'utente ha il permesso di accedere a questa risorsa.
        User user = userService.findById(id);
        return ResponseEntity.ok(convertToResponseDto(user));
    }

    // Registra un nuovo utente nel sistema.
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserDto userDto) {
        User createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponseDto(createdUser));
    }

    // Aggiorna i dati di un utente esistente
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        checkUserPermission(id);
        User updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(convertToResponseDto(updatedUser));
    }

    @DeleteMapping("/delete/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        // Aggiungo un controllo per non permettere a un utente di auto-eliminarsi
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User userToDelete = userService.findById(id);
        if (userToDelete.getEmail().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Non puoi eliminare il tuo stesso account."));
        }

        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "Utente eliminato con successo"));
    }

    // --- Gestione Centralizzata delle Eccezioni (invariata) ---
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, EmailAlreadyUsedException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, String>> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Accesso negato: " + ex.getMessage()));
    }

    // --- Metodi Helper ---

    private void checkUserPermission(Long targetUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return;
        }

        String currentUsername = authentication.getName();
        User currentUser = userService.findByEmail(currentUsername)
                .orElseThrow(() -> new UserNotFoundException((String) null));

        if (!currentUser.getId().equals(targetUserId)) {
            throw new AccessDeniedException("Non hai i permessi per accedere a questa risorsa.");
        }
    }

    // NUOVO METODO HELPER per convertire User in UserResponseDto
    private UserResponseDto convertToResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        if (user.getRole() != null) {
            dto.setRole(user.getRole().name());
        }
        return dto;
    }
}