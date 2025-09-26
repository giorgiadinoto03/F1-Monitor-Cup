package its.fullstack.biblioteca_proj.service;

import its.fullstack.biblioteca_proj.exception.BookNotAvailableException;
import its.fullstack.biblioteca_proj.exception.BookNotFoundException;
import its.fullstack.biblioteca_proj.exception.PrestitoNotFoundException;
import its.fullstack.biblioteca_proj.exception.UserNotFoundException;
import its.fullstack.biblioteca_proj.models.Book;
import its.fullstack.biblioteca_proj.models.Prestito;
import its.fullstack.biblioteca_proj.models.User;
import its.fullstack.biblioteca_proj.repository.BookRepository;
import its.fullstack.biblioteca_proj.repository.PrestitoRepository;
import its.fullstack.biblioteca_proj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestitoService {

    @Autowired
    private PrestitoRepository prestitoRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public static final int GIORNI_PRESTITO = 30;

    @Transactional
    public Prestito creaPrestito(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // RIMOSSO: Il controllo sull'utente attivo non è più necessario
        // if (!user.isAttivo()) {
        //     throw new IllegalStateException("Impossibile effettuare il prestito: utente disattivato");
        // }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        if (!book.isDisponibile()) {
            throw new BookNotAvailableException(bookId);
        }

        if (prestitoRepository.existsByUserIdAndBookIdAndNotRestituito(userId, bookId)) {
            throw new IllegalStateException("L'utente ha già preso in prestito questo libro e non l'ha ancora restituito");
        }

        Prestito prestito = new Prestito();
        prestito.setUser(user);
        prestito.setBook(book);
        prestito.setDataPrestito(LocalDate.now());
        prestito.setDataRestituzione(LocalDate.now().plusDays(GIORNI_PRESTITO));
        prestito.setRestituito(false);

        book.setDisponibile(false);
        bookRepository.save(book);

        return prestitoRepository.save(prestito);
    }

    @Transactional
    public Prestito restituisciLibro(Long userId, Long bookId) {
        Prestito prestito = prestitoRepository.findByUserIdAndBookIdAndRestituitoFalse(userId, bookId)
                .orElseThrow(() -> new RuntimeException("Prestito non trovato per questo utente e libro"));

        prestito.setRestituito(true);
        prestito.setDataRestituzione(LocalDate.now());

        Book book = prestito.getBook();
        book.setDisponibile(true);
        bookRepository.save(book);

        return prestitoRepository.save(prestito);
    }

    public List<Prestito> getPrestitiByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return prestitoRepository.findByUserId(userId);
    }

    public List<Prestito> getPrestitiUtenteAttivi(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return prestitoRepository.findByUserIdAndRestituito(userId, false);
    }

    public List<Prestito> getAllPrestiti() {
        return prestitoRepository.findAll();
    }
}