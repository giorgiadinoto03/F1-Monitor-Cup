package its.fullstack.biblioteca_proj.exception;

public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException() {
        super("Utente non trovato");
    }
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UserNotFoundException(Long id) {
        super("Utente con ID " + id + " non trovato");
    }
    
    public UserNotFoundException(String email, boolean isEmail) {
        super("Utente con email " + email + " non trovato");
    }
}
