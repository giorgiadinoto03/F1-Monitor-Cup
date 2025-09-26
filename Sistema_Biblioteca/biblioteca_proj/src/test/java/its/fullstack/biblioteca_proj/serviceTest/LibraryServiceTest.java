package its.fullstack.biblioteca_proj.serviceTest;

import its.fullstack.biblioteca_proj.dto.BookDTO;
import its.fullstack.biblioteca_proj.dto.BookMapper;
import its.fullstack.biblioteca_proj.models.Book;
import its.fullstack.biblioteca_proj.repository.BookRepository;
import its.fullstack.biblioteca_proj.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test per la classe LibraryService.
 * Verifica il corretto funzionamento dei metodi di gestione dei libri.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Test per LibraryService")
class LibraryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private LibraryService libraryService;

    private Book testBook;
    private BookDTO testBookDTO;
    private List<Book> testBooks;
    private List<BookDTO> testBookDTOs;

    /**
     * Configurazione iniziale per i test.
     * Inizializza i dati di test prima di ogni test.
     */
    @BeforeEach
    void setUp() {
        testBook = new Book("Test Book", "Test Author", "Fiction", 2023);
        testBook.setId(1L);
        testBook.setDisponibile(true);

        testBookDTO = new BookDTO(testBook);
        testBookDTO.setDisponibile(true);

        Book anotherBook = new Book("Another Book", "Another Author", "Non-Fiction", 2022);
        anotherBook.setId(2L);
        anotherBook.setDisponibile(false);

        testBooks = Arrays.asList(testBook, anotherBook);
        testBookDTOs = Arrays.asList(testBookDTO, new BookDTO(anotherBook));
    }

    @Test
    @DisplayName("getAllBooksDTO dovrebbe restituire tutti i libri come DTO")
    void getAllBooksDTO_ShouldReturnAllBooksAsDTO() {
        // Given
        when(bookRepository.findAll()).thenReturn(testBooks);
        when(bookMapper.toDTOList(testBooks)).thenReturn(testBookDTOs);

        // When
        List<BookDTO> result = libraryService.getAllBooksDTO();

        // Then
        assertNotNull(result, "La lista restituita non dovrebbe essere nulla");
        assertEquals(2, result.size(), "Dovrebbero esserci 2 libri nella lista");
        verify(bookRepository).findAll();
        verify(bookMapper).toDTOList(testBooks);
    }

    @Test
    @DisplayName("getBookByIdDTO dovrebbe restituire un libro quando esiste")
    void getBookByIdDTO_WhenBookExists_ShouldReturnBookDTO() {
        // Given
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
        when(bookMapper.toDTO(testBook)).thenReturn(testBookDTO);

        // When
        Optional<BookDTO> result = libraryService.getBookByIdDTO(bookId);

        // Then
        assertTrue(result.isPresent(), "Dovrebbe essere presente un libro");
        assertEquals(testBookDTO, result.get(), "Il libro restituito non corrisponde a quello atteso");
        verify(bookRepository).findById(bookId);
        verify(bookMapper).toDTO(testBook);
    }

    @Test
    @DisplayName("getBookByIdDTO dovrebbe restituire Optional vuoto quando il libro non esiste")
    void getBookByIdDTO_WhenBookNotExists_ShouldReturnEmpty() {
        // Given
        Long bookId = 999L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When
        Optional<BookDTO> result = libraryService.getBookByIdDTO(bookId);

        // Then
        assertFalse(result.isPresent(), "Non dovrebbe esserci alcun libro");
        verify(bookRepository).findById(bookId);
        verify(bookMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("getBooksByTitleDTO dovrebbe restituire i libri che corrispondono al titolo")
    void getBooksByTitleDTO_ShouldReturnBooksByTitle() {
        // Given
        String title = "Test Book";
        when(bookRepository.findByTitleContainingIgnoreCase(title)).thenReturn(Arrays.asList(testBook));
        when(bookMapper.toDTOList(Arrays.asList(testBook))).thenReturn(Arrays.asList(testBookDTO));

        // When
        List<BookDTO> result = libraryService.getBooksByTitleDTO(title);

        // Then
        assertNotNull(result, "La lista restituita non dovrebbe essere nulla");
        assertEquals(1, result.size(), "Dovrebbe esserci 1 libro nella lista");
        assertEquals(testBookDTO.getName(), result.get(0).getName(), "Il titolo del libro non corrisponde");
        verify(bookRepository).findByTitleContainingIgnoreCase(title);
    }

    @Test
    @DisplayName("getBooksByAuthorDTO dovrebbe restituire i libri dell'autore specificato")
    void getBooksByAuthorDTO_ShouldReturnBooksByAuthor() {
        // Given
        String author = "Test Author";
        when(bookRepository.findByAuthorContainingIgnoreCase(author)).thenReturn(Arrays.asList(testBook));
        when(bookMapper.toDTOList(Arrays.asList(testBook))).thenReturn(Arrays.asList(testBookDTO));

        // When
        List<BookDTO> result = libraryService.getBooksByAuthorDTO(author);

        // Then
        assertNotNull(result, "La lista restituita non dovrebbe essere nulla");
        assertEquals(1, result.size(), "Dovrebbe esserci 1 libro nella lista");
        assertEquals(testBookDTO.getAuthor(), result.get(0).getAuthor(), "L'autore del libro non corrisponde");
        verify(bookRepository).findByAuthorContainingIgnoreCase(author);
    }

    @Test
    @DisplayName("getBooksByCategoryDTO dovrebbe restituire i libri della categoria specificata")
    void getBooksByCategoryDTO_ShouldReturnBooksByCategory() {
        // Given
        String category = "Fiction";
        when(bookRepository.findByCategory(category)).thenReturn(Arrays.asList(testBook));
        when(bookMapper.toDTOList(Arrays.asList(testBook))).thenReturn(Arrays.asList(testBookDTO));

        // When
        List<BookDTO> result = libraryService.getBooksByCategoryDTO(category);

        // Then
        assertNotNull(result, "La lista restituita non dovrebbe essere nulla");
        assertEquals(1, result.size(), "Dovrebbe esserci 1 libro nella lista");
        assertEquals(testBookDTO.getCategory(), result.get(0).getCategory(), "La categoria del libro non corrisponde");
        verify(bookRepository).findByCategory(category);
    }

    @Test
    @DisplayName("getBooksByYearDTO dovrebbe restituire i libri dell'anno specificato")
    void getBooksByYearDTO_ShouldReturnBooksByYear() {
        // Given
        int year = 2023;
        when(bookRepository.findByYear(year)).thenReturn(Arrays.asList(testBook));
        when(bookMapper.toDTOList(Arrays.asList(testBook))).thenReturn(Arrays.asList(testBookDTO));

        // When
        List<BookDTO> result = libraryService.getBooksByYearDTO(year);

        // Then
        assertNotNull(result, "La lista restituita non dovrebbe essere nulla");
        assertEquals(1, result.size(), "Dovrebbe esserci 1 libro nella lista");
        assertEquals(year, result.get(0).getYear(), "L'anno del libro non corrisponde");
        verify(bookRepository).findByYear(year);
    }

    @Test
    @DisplayName("getAvailableBooksDTO dovrebbe restituire solo i libri disponibili")
    void getAvailableBooksDTO_ShouldReturnOnlyAvailableBooks() {
        // Given
        when(bookRepository.findByDisponibileTrue()).thenReturn(Arrays.asList(testBook));
        when(bookMapper.toDTOList(Arrays.asList(testBook))).thenReturn(Arrays.asList(testBookDTO));

        // When
        List<BookDTO> result = libraryService.getAvailableBooksDTO();

        // Then
        assertNotNull(result, "La lista restituita non dovrebbe essere nulla");
        assertEquals(1, result.size(), "Dovrebbe esserci 1 libro disponibile");
        assertTrue(result.get(0).isDisponibile(), "Il libro dovrebbe essere contrassegnato come disponibile");
        verify(bookRepository).findByDisponibileTrue();
    }

    @Test
    @DisplayName("createBook dovrebbe creare un nuovo libro con disponibilità impostata a true")
    void createBook_ShouldCreateNewBookWithAvailabilityTrue() {
        // Given
        Book newBook = new Book("New Book", "New Author", "New Category", 2024);
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        // When
        Book result = libraryService.createBook("Test message", newBook);

        // Then
        assertNotNull(result, "Il libro creato non dovrebbe essere nullo");
        assertTrue(result.isDisponibile(), "Il nuovo libro dovrebbe essere disponibile");
        verify(bookRepository).save(newBook);
    }

    @Test
    @DisplayName("updateBook dovrebbe aggiornare un libro esistente")
    void updateBook_WhenBookExists_ShouldUpdateBook() {
        // Given
        Long bookId = 1L;
        Book updatedDetails = new Book("Updated Book", "Updated Author", "Updated Category", 2023);
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // When
        Book result = libraryService.updateBook("Test message", bookId, updatedDetails);

        // Then
        assertNotNull(result, "Il libro aggiornato non dovrebbe essere nullo");
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(testBook);
    }

    @Test
    @DisplayName("updateBook dovrebbe lanciare un'eccezione quando il libro non esiste")
    void updateBook_WhenBookNotExists_ShouldThrowException() {
        // Given
        Long bookId = 999L;
        Book updatedDetails = new Book("Nonexistent Book", "Nonexistent Author", "Nonexistent Category", 2023);
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, 
            () -> libraryService.updateBook("Test message", bookId, updatedDetails)
        );
        
        assertEquals("Libro non trovato con id: " + bookId, exception.getMessage(),
            "Il messaggio di errore dovrebbe indicare che il libro non è stato trovato");
        
        verify(bookRepository).findById(bookId);
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteBook dovrebbe eliminare un libro esistente e disponibile")
    void deleteBook_WhenBookExistsAndAvailable_ShouldDeleteBook() {
        // Given
        Long bookId = 1L;
        testBook.setDisponibile(true);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));

        // When
        libraryService.deleteBook(bookId);

        // Then
        verify(bookRepository).findById(bookId);
        verify(bookRepository).delete(testBook);
    }

    @Test
    @DisplayName("deleteBook dovrebbe lanciare un'eccezione quando il libro non è disponibile")
    void deleteBook_WhenBookNotAvailable_ShouldThrowException() {
        // Given
        Long bookId = 1L;
        testBook.setDisponibile(false);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, 
            () -> libraryService.deleteBook(bookId)
        );
        
        assertEquals("Impossibile eliminare il libro: è attualmente in prestito", exception.getMessage(),
            "Il messaggio di errore dovrebbe indicare che il libro è in prestito");
        
        verify(bookRepository).findById(bookId);
        verify(bookRepository, never()).delete(any());
    }

    @Test
    @DisplayName("deleteBook dovrebbe lanciare un'eccezione quando il libro non esiste")
    void deleteBook_WhenBookNotExists_ShouldThrowException() {
        // Given
        Long bookId = 999L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, 
            () -> libraryService.deleteBook(bookId)
        );
        
        assertEquals("Libro non trovato con id: " + bookId, exception.getMessage(),
            "Il messaggio di errore dovrebbe indicare che il libro non è stato trovato");
        
        verify(bookRepository).findById(bookId);
        verify(bookRepository, never()).delete(any());
    }
}