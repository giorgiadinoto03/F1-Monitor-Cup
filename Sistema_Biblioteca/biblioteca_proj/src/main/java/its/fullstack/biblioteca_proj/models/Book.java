package its.fullstack.biblioteca_proj.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Il nome del libro è obbligatorio")
    @Size(max = 255, message = "Il nome non può superare i 255 caratteri")
    private String name;

    @Column(name = "author", nullable = false)
    @NotBlank(message = "L'autore è obbligatorio")
    @Size(max = 255, message = "Il nome dell'autore non può superare i 255 caratteri")
    private String author;

    @Column(name = "category", nullable = false)
    @NotBlank(message = "La categoriaè obbligatoria")
    private String category;

    @Column(name = "year")
    private int year;

    @Column(name = "disponibile", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean disponibile = true;


    // CORRETTA: Ignora completamente i prestiti per evitare loop infiniti
    @OneToMany(mappedBy = "book")
    @JsonIgnore  // IMPEDISCE la serializzazione dei prestiti
    private List<Prestito> prestiti = new ArrayList<>();


    // Costruttori
    public Book() {
    }

    public Book(String name, String author,String category, int year) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.year = year;
        this.disponibile = true;


    }


    //GETTER E SETTER
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public List<Prestito> getPrestiti() {
        return prestiti;
    }

    public void setPrestiti(List<Prestito> prestiti) {
        this.prestiti = prestiti;
    }


    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", year=" + year +
                ", disponibile=" + disponibile +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id != null && id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : getClass().hashCode();
    }
}