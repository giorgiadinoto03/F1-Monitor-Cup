package its.fullstack.biblioteca_proj.serviceTest;

import its.fullstack.biblioteca_proj.exception.BookNotAvailableException;
import its.fullstack.biblioteca_proj.exception.BookNotFoundException;
import its.fullstack.biblioteca_proj.exception.UserNotFoundException;
import its.fullstack.biblioteca_proj.models.Book;
import its.fullstack.biblioteca_proj.models.Prestito;
import its.fullstack.biblioteca_proj.models.Ruolo;
import its.fullstack.biblioteca_proj.models.User;
import its.fullstack.biblioteca_proj.repository.BookRepository;
import its.fullstack.biblioteca_proj.repository.PrestitoRepository;
import its.fullstack.biblioteca_proj.repository.UserRepository;
import its.fullstack.biblioteca_proj.service.PrestitoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrestitoServiceTest {

    @Mock
    private PrestitoRepository prestitoRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PrestitoService prestitoService;

    private User testUser;
    private Book testBook;
    private Prestito testPrestito;

    @BeforeEach
    void setUp() {
        testUser = new User("Mario", "Rossi", 30, "mario@test.com", "password", Ruolo.CLIENT);
        testUser.setId(1L);

        testBook = new Book("Test Book", "Test Author", "Fiction", 2023);
        testBook.setId(1L);
        testBook.setDisponibile(true);

        testPrestito = new Prestito();
        testPrestito.setId(1L);
        testPrestito.setUser(testUser);
        testPrestito.setBook(testBook);
        testPrestito.setDataPrestito(LocalDate.now());
        testPrestito.setDataRestituzione(LocalDate.now().plusDays(PrestitoService.GIORNI_PRESTITO));
        testPrestito.setRestituito(false);
    }

    @Test
    void creaPrestito_WhenValidUserAndAvailableBook_ShouldCreatePrestito() {
        // Given
        Long userId = 1L;
        Long bookId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
        when(prestitoRepository.existsByUserIdAndBookIdAndNotRestituito(userId, bookId)).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(prestitoRepository.save(any(Prestito.class))).thenReturn(testPrestito);

        // When
        Prestito result = prestitoService.creaPrestito(userId, bookId);

        // Then
        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertEquals(testBook, result.getBook());
        assertFalse(result.isRestituito());
        assertFalse(testBook.isDisponibile());

        verify(userRepository).findById(userId);
        verify(bookRepository).findById(bookId);
        verify(prestitoRepository).existsByUserIdAndBookIdAndNotRestituito(userId, bookId);
        verify(bookRepository).save(testBook);
        verify(prestitoRepository).save(any(Prestito.class));
    }

    @Test
    void creaPrestito_WhenUserNotFound_ShouldThrowUserNotFoundException() {
        // Given
        Long userId = 999L;
        Long bookId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class,
                () -> prestitoService.creaPrestito(userId, bookId));

        verify(userRepository).findById(userId);
        verify(bookRepository, never()).findById(any());
        verify(prestitoRepository, never()).save(any());
    }

    @Test
    void creaPrestito_WhenBookNotFound_ShouldThrowBookNotFoundException() {
        // Given
        Long userId = 1L;
        Long bookId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookNotFoundException.class,
                () -> prestitoService.creaPrestito(userId, bookId));

        verify(userRepository).findById(userId);
        verify(bookRepository).findById(bookId);
        verify(prestitoRepository, never()).save(any());
    }

    @Test
    void creaPrestito_WhenBookNotAvailable_ShouldThrowBookNotAvailableException() {
        // Given
        Long userId = 1L;
        Long bookId = 1L;
        testBook.setDisponibile(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));

        // When & Then
        assertThrows(BookNotAvailableException.class,
                () -> prestitoService.creaPrestito(userId, bookId));

        verify(userRepository).findById(userId);
        verify(bookRepository).findById(bookId);
        verify(prestitoRepository, never()).save(any());
    }

    @Test
    void creaPrestito_WhenUserAlreadyHasBook_ShouldThrowIllegalStateException() {
        // Given
        Long userId = 1L;
        Long bookId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
        when(prestitoRepository.existsByUserIdAndBookIdAndNotRestituito(userId, bookId)).thenReturn(true);

        // When & Then
        assertThrows(IllegalStateException.class,
                () -> prestitoService.creaPrestito(userId, bookId));

        verify(prestitoRepository).existsByUserIdAndBookIdAndNotRestituito(userId, bookId);
        verify(prestitoRepository, never()).save(any());
    }

    @Test
    void restituisciLibro_WhenValidPrestito_ShouldReturnBook() {
        // Given
        Long userId = 1L;
        Long bookId = 1L;
        when(prestitoRepository.findByUserIdAndBookIdAndRestituitoFalse(userId, bookId))
                .thenReturn(Optional.of(testPrestito));
        when(prestitoRepository.save(any(Prestito.class))).thenReturn(testPrestito);

        // When
        Prestito result = prestitoService.restituisciLibro(userId, bookId);

        // Then
        assertNotNull(result);
        assertTrue(result.isRestituito());
        assertEquals(LocalDate.now(), result.getDataRestituzione());

        verify(prestitoRepository).findByUserIdAndBookIdAndRestituitoFalse(userId, bookId);
        verify(prestitoRepository).save(testPrestito);
    }

    @Test
    void restituisciLibro_WhenPrestitoNotFound_ShouldThrowRuntimeException() {
        // Given
        Long userId = 1L;
        Long bookId = 1L;
        when(prestitoRepository.findByUserIdAndBookIdAndRestituitoFalse(userId, bookId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class,
                () -> prestitoService.restituisciLibro(userId, bookId));

        verify(prestitoRepository).findByUserIdAndBookIdAndRestituitoFalse(userId, bookId);
        verify(prestitoRepository, never()).save(any());
    }

    @Test
    void getPrestitiByUserId_WhenUserExists_ShouldReturnPrestiti() {
        // Given
        Long userId = 1L;
        List<Prestito> expectedPrestiti = Arrays.asList(testPrestito);
        when(userRepository.existsById(userId)).thenReturn(true);
        when(prestitoRepository.findByUserId(userId)).thenReturn(expectedPrestiti);

        // When
        List<Prestito> result = prestitoService.getPrestitiByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPrestito, result.get(0));

        verify(userRepository).existsById(userId);
        verify(prestitoRepository).findByUserId(userId);
    }

    @Test
    void getPrestitiByUserId_WhenUserNotExists_ShouldThrowUserNotFoundException() {
        // Given
        Long userId = 999L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // When & Then
        assertThrows(UserNotFoundException.class,
                () -> prestitoService.getPrestitiByUserId(userId));

        verify(userRepository).existsById(userId);
        verify(prestitoRepository, never()).findByUserId(any());
    }

    @Test
    void getPrestitiUtenteAttivi_WhenUserExists_ShouldReturnActivePrestiti() {
        // Given
        Long userId = 1L;
        List<Prestito> expectedPrestiti = Arrays.asList(testPrestito);
        when(userRepository.existsById(userId)).thenReturn(true);
        when(prestitoRepository.findByUserIdAndRestituito(userId, false)).thenReturn(expectedPrestiti);

        // When
        List<Prestito> result = prestitoService.getPrestitiUtenteAttivi(userId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).isRestituito());

        verify(userRepository).existsById(userId);
        verify(prestitoRepository).findByUserIdAndRestituito(userId, false);
    }

    @Test
    void getPrestitiUtenteAttivi_WhenUserNotExists_ShouldThrowUserNotFoundException() {
        // Given
        Long userId = 999L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // When & Then
        assertThrows(UserNotFoundException.class,
                () -> prestitoService.getPrestitiUtenteAttivi(userId));

        verify(userRepository).existsById(userId);
        verify(prestitoRepository, never()).findByUserIdAndRestituito(any(), anyBoolean());
    }

    @Test
    void getAllPrestiti_ShouldReturnAllPrestiti() {
        // Given
        List<Prestito> expectedPrestiti = Arrays.asList(testPrestito);
        when(prestitoRepository.findAll()).thenReturn(expectedPrestiti);

        // When
        List<Prestito> result = prestitoService.getAllPrestiti();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPrestito, result.get(0));

        verify(prestitoRepository).findAll();
    }
}