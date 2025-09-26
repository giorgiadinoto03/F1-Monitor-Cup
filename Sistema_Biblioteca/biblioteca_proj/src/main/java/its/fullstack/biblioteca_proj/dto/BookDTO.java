package its.fullstack.biblioteca_proj.dto;

import its.fullstack.biblioteca_proj.models.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private Long id;
    private String name;
    private String author;
    private String category;
    private int year;
    private boolean disponibile;

    public BookDTO(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.author = book.getAuthor();
        this.category = book.getCategory();
        this.year = book.getYear();
        this.disponibile = book.isDisponibile();
    }
}