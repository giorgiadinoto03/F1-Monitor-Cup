package its.fullstack.biblioteca_proj.exceptionTest;

import its.fullstack.biblioteca_proj.exception.BookNotAvailableException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la classe BookNotAvailableException.
 * Verifica il corretto funzionamento dell'eccezione personalizzata per libri non disponibili.
 */
class BookNotAvailableExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        // When
        BookNotAvailableException exception = new BookNotAvailableException();

        // Then
        assertNotNull(exception);
        assertEquals("Il libro non è disponibile per il prestito", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCustomMessage() {
        // Given
        String customMessage = "Libro già in prestito";

        // When
        BookNotAvailableException exception = new BookNotAvailableException(customMessage);

        // Then
        assertNotNull(exception);
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCause() {
        // Given
        String customMessage = "Errore di disponibilità";
        Throwable cause = new RuntimeException("Causa radice");

        // When
        BookNotAvailableException exception = new BookNotAvailableException(customMessage, cause);

        // Then
        assertNotNull(exception);
        assertEquals(customMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
