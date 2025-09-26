package its.fullstack.biblioteca_proj.exceptionTest;

import its.fullstack.biblioteca_proj.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la classe UserNotFoundException.
 * Verifica il corretto funzionamento dell'eccezione personalizzata per utenti non trovati.
 */
class UserNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        // When
        UserNotFoundException exception = new UserNotFoundException();

        // Then
        assertNotNull(exception);
        assertEquals("Utente non trovato", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCustomMessage() {
        // Given
        String customMessage = "Utente con ID 789 non trovato";

        // When
        UserNotFoundException exception = new UserNotFoundException(customMessage);

        // Then
        assertNotNull(exception);
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithId() {
        // Given
        Long userId = 789L;

        // When
        UserNotFoundException exception = new UserNotFoundException(userId);

        // Then
        assertNotNull(exception);
        assertEquals("Utente con ID " + userId + " non trovato", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithEmail() {
        // Given
        String email = "user@example.com";

        // When
        UserNotFoundException exception = new UserNotFoundException(email);

        // Then
        assertNotNull(exception);
        assertEquals("Utente con email " + email + " non trovato", exception.getMessage());
    }
}
