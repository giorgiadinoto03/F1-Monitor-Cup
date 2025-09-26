package its.fullstack.biblioteca_proj.exceptionTest;

import its.fullstack.biblioteca_proj.exception.BookNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la classe BookNotFoundException.
 * Verifica il corretto funzionamento dell'eccezione personalizzata per libri non trovati.
 */
class BookNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        // When
        BookNotFoundException exception = new BookNotFoundException();

        // Then
        assertNotNull(exception);
        assertEquals("Libro non trovato", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCustomMessage() {
        // Given
        String customMessage = "Libro con ID 123 non trovato";

        // When
        BookNotFoundException exception = new BookNotFoundException(customMessage);

        // Then
        assertNotNull(exception);
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithId() {
        // Given
        Long bookId = 123L;

        // When
        BookNotFoundException exception = new BookNotFoundException(bookId);

        // Then
        assertNotNull(exception);
        assertEquals("Libro con ID " + bookId + " non trovato", exception.getMessage());
    }
}
