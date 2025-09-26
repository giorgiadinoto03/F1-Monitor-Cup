package its.fullstack.biblioteca_proj.controller;

import its.fullstack.biblioteca_proj.dto.LoginResponse;
import its.fullstack.biblioteca_proj.models.User;
import its.fullstack.biblioteca_proj.security.CustomUserDetailsService;
import its.fullstack.biblioteca_proj.security.JwtUtil;
import its.fullstack.biblioteca_proj.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")    // Prefisso per tutte le rotte
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 1. Tenta di autenticare l'utente usando l'AuthenticationManager di Spring Security.
        //    Questo verifica che l'email e la password fornite siano corrette.
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        // 2. Se le credenziali non sono valide, l'AuthenticationManager lancia una BadCredentialsException.
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali non valide");
        }

        // 3. Se l'autenticazione ha successo, carica i dettagli dell'utente.
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        // 4. Genera un token JWT per l'utente autenticato, includendo il prefisso "Bearer ".
        final String token = "Bearer " + jwtUtil.generateToken(userDetails);

        // 5. Recupera l'entità User completa per ottenere informazioni aggiuntive (es. ruolo).
        User user = userService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + loginRequest.getEmail()));

        // 6. Costruisce la risposta da inviare al client, contenente le informazioni essenziali e il token.
        LoginResponse response = new LoginResponse(
                user.getEmail(),
                user.getRole().name(),
                token
        );

        return ResponseEntity.ok(response);
    }

    // DTO per la richiesta di login
    public static class LoginRequest {
        private String email;
        private String password;

        // Getter e Setter
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
