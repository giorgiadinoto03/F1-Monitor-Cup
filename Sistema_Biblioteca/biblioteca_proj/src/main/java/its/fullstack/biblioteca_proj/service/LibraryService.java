package its.fullstack.biblioteca_proj.service;

import its.fullstack.biblioteca_proj.dto.BookDTO;
import its.fullstack.biblioteca_proj.dto.BookMapper;
import its.fullstack.biblioteca_proj.models.Book;
import its.fullstack.biblioteca_proj.repository.BookRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;


    // Metodi DTO
    public List<BookDTO> getAllBooksDTO() {
        return bookMapper.toDTOList(bookRepository.findAll());
    }

    public Optional<BookDTO> getBookByIdDTO(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDTO);
    }

    public List<BookDTO> getBooksByAuthorDTO(String author) {
        List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);
        return bookMapper.toDTOList(books);
    }

    public List<BookDTO> getBooksByTitleDTO(String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        return bookMapper.toDTOList(books);
    }

    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategory(category);
    }

    public List<BookDTO> getBooksByCategoryDTO(String category) {
        List<Book> books = bookRepository.findByCategory(category);
        return bookMapper.toDTOList(books);
    }

    public List<BookDTO> getBooksByYearDTO(int year) {
        List<Book> books = bookRepository.findByYear(year);
        return bookMapper.toDTOList(books);
    }

    public List<BookDTO> getAvailableBooksDTO() {
        return bookMapper.toDTOList(bookRepository.findByDisponibileTrue());
    }


    // Metodi per la gestione dei libri
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> getBooksByYear(int year) {
        return bookRepository.findByYear(year);
    }

    public List<Book> bookListService() {
        return getAllBooks();
    }

    /*
     * Restituisce tutti i libri disponibili per il prestito
     */

    /*
     * Crea un nuovo libro
     */
    @Transactional
    public Book createBook(String s, Book book) {
        // Di default un nuovo libro è disponibile
        book.setDisponibile(true);
        return bookRepository.save(book);
    }

    /**
     * Aggiorna un libro esistente
     */
    @Transactional
    public Book updateBook(String s, Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro non trovato con id: " + id));

        // Aggiorna i campi del libro
        book.setName(bookDetails.getName());
        book.setAuthor(bookDetails.getAuthor());
        book.setYear(bookDetails.getYear());

        // Non aggiorniamo la disponibilità qui, va gestita con i prestiti

        return bookRepository.save(book);
    }

    /**
     * Elimina un libro
     */
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro non trovato con id: " + id));

        // Verifica se il libro è attualmente in prestito
        if (!book.isDisponibile()) {
            throw new RuntimeException("Impossibile eliminare il libro: è attualmente in prestito");
        }

        bookRepository.delete(book);
    }
}