package its.fullstack.biblioteca_proj.controller;

import its.fullstack.biblioteca_proj.dto.BookDTO;
import its.fullstack.biblioteca_proj.dto.PrestitoDTO;
import its.fullstack.biblioteca_proj.exception.BookNotAvailableException;
import its.fullstack.biblioteca_proj.exception.BookNotFoundException;
import its.fullstack.biblioteca_proj.exception.PrestitoNotFoundException;
import its.fullstack.biblioteca_proj.exception.UserNotFoundException;
import its.fullstack.biblioteca_proj.models.Book;
import its.fullstack.biblioteca_proj.models.Prestito;
import its.fullstack.biblioteca_proj.models.User;
import its.fullstack.biblioteca_proj.service.LibraryService;
import its.fullstack.biblioteca_proj.service.PrestitoService;
import its.fullstack.biblioteca_proj.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    // Restituisce tutti i libri
    @GetMapping("/all")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(libraryService.getAllBooksDTO());
    }

    // Restituisce un libro cercato per ID
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return libraryService.getBookByIdDTO(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Ricerca libri per autore
    @PostMapping("/search/author")
    public ResponseEntity<List<BookDTO>> searchByAuthor(@RequestBody Map<String, String> body) {
        String author = body.get("author");
        if (author == null || author.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(List.of());
        }
        return ResponseEntity.ok(libraryService.getBooksByAuthorDTO(author.trim()));
    }

    // Ricerca libri per titolo
    @PostMapping("/search/title")
    public ResponseEntity<List<BookDTO>> searchByTitle(@RequestBody Map<String, String> body) {
        String title = body.get("title");
        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(List.of());
        }
        return ResponseEntity.ok(libraryService.getBooksByTitleDTO(title.trim()));
    }

    // Ricerca libri per anno
    @PostMapping("/search/year")
    public ResponseEntity<List<BookDTO>> searchByYear(@RequestBody Map<String, String> body) {
        String yearStr = body.get("year");
        if (yearStr == null || yearStr.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(List.of());
        }
        try {
            int year = Integer.parseInt(yearStr.trim());
            return ResponseEntity.ok(libraryService.getBooksByYearDTO(year));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    // Restituisce tutti i libri che sono disponibili per il prestito non presi in prestito
    @GetMapping("/available")
    public ResponseEntity<List<BookDTO>> getAvailableBooks() {
        return ResponseEntity.ok(libraryService.getAvailableBooksDTO());
    }

    // ENDPOINTS SOLO PER ADMIN - CREAZIONE, AGGIORNAMENTO ED ELIMINAZIONE LIBRI
    // Creare un nuovo libro
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.ok(libraryService.createBook("Libro creato in: " , book));
    }

    // Aggiornare un libro
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        try {
            return ResponseEntity.ok(libraryService.updateBook("Libro aggiornato in: " , id, book));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminare un libro
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Map<String, String>> deleteBook(@PathVariable Long id) {
        try {
            libraryService.deleteBook(id);
            return ResponseEntity.ok(Map.of("message", "Libro eliminato con successo"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Libro non trovato"));
        }
    }
}