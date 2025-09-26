package its.fullstack.biblioteca_proj.exceptionTest;

import its.fullstack.biblioteca_proj.exception.EmailAlreadyUsedException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la classe EmailAlreadyUsedException.
 * Verifica il corretto funzionamento dell'eccezione personalizzata per email già in uso.
 */
class EmailAlreadyUsedExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        // When
        EmailAlreadyUsedException exception = new EmailAlreadyUsedException();

        // Then
        assertNotNull(exception);
        assertEquals("Email già in uso", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCustomMessage() {
        // Given
        String customMessage = "L'email test@example.com è già registrata";

        // When
        EmailAlreadyUsedException exception = new EmailAlreadyUsedException(customMessage);

        // Then
        assertNotNull(exception);
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithEmail() {
        // Given
        String email = "test@example.com";

        // When
        EmailAlreadyUsedException exception = new EmailAlreadyUsedException();

        // Then
        assertNotNull(exception);
        assertEquals("L'email " + email + " è già in uso", exception.getMessage());
    }
}
