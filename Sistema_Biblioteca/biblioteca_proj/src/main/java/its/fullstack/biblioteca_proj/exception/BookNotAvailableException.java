package its.fullstack.biblioteca_proj.exception;

public class BookNotAvailableException extends RuntimeException {
    
    public BookNotAvailableException() {
        super("Il libro non è disponibile per il prestito");
    }
    
    public BookNotAvailableException(String message) {
        super(message);
    }
    
    public BookNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BookNotAvailableException(Long id) {
        super("Il libro con id " + id + " non è attualmente disponibile per il prestito");
    }
}