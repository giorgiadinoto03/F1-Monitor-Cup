package its.fullstack.biblioteca_proj.exception;

public class PrestitoNotFoundException extends RuntimeException {
    
    public PrestitoNotFoundException() {
        super("Prestito non trovato");
    }
    
    public PrestitoNotFoundException(String message) {
        super(message);
    }
    
    public PrestitoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public PrestitoNotFoundException(Long id) {
        super("Prestito con ID " + id + " non trovato");
    }
}
