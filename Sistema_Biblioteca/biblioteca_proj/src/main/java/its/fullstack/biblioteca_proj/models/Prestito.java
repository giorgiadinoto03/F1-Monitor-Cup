package its.fullstack.biblioteca_proj.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prestiti")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Prestito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_prestito")
    private LocalDate dataPrestito;

    @Column(name = "data_restituzione")
    private LocalDate dataRestituzione;

    private boolean restituito;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"prestiti", "password", "hibernateLazyInitializer", "handler"})
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    @JsonIgnoreProperties({"prestiti", "categorie", "hibernateLazyInitializer", "handler"})
    private Book book;

    public Prestito() {
    }

    public Prestito(User user, Book book, LocalDate dataPrestito, LocalDate dataRestituzione) {
        this.user = user;
        this.book = book;
        this.dataPrestito = dataPrestito;
        this.dataRestituzione = dataRestituzione;
        this.restituito = false;
    }

    // --- GETTER E SETTER ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getDataPrestito() {
        return dataPrestito;
    }

    public void setDataPrestito(LocalDate dataPrestito) {
        this.dataPrestito = dataPrestito;
    }

    public LocalDate getDataRestituzione() {
        return dataRestituzione;
    }

    public void setDataRestituzione(LocalDate dataRestituzione) {
        this.dataRestituzione = dataRestituzione;
    }

    public boolean isRestituito() {
        return restituito;
    }

    public void setRestituito(boolean restituito) {
        this.restituito = restituito;
        if (restituito && this.dataRestituzione == null) {
            this.dataRestituzione = LocalDate.now();
        }
    }
}