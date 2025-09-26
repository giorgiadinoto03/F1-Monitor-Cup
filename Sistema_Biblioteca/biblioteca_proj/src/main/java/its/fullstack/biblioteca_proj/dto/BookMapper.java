package its.fullstack.biblioteca_proj.dto;

import its.fullstack.biblioteca_proj.models.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookDTO toDTO(Book book) {   // Book -> BookDTO
        return new BookDTO(book);
    }

    public List<BookDTO> toDTOList(List<Book> books) {  // List<Book> -> List<BookDTO>
        return books.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }   //Questo metodo serve a convertire una lista di oggetti Book in una lista di oggetti BookDTO
}