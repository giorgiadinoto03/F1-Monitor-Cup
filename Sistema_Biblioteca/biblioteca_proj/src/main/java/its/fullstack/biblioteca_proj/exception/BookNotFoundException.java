package its.fullstack.biblioteca_proj.exception;

public class BookNotFoundException extends RuntimeException {
    
    public BookNotFoundException() {
        super("Libro non trovato");
    }
    
    public BookNotFoundException(String message) {
        super(message);
    }
    
    public BookNotFoundException(Long id) {
        super("Libro con ID " + id + " non trovato");
    }
}