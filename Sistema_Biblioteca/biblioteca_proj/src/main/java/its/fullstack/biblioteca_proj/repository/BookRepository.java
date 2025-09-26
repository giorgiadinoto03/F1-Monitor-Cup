package its.fullstack.biblioteca_proj.repository;

import its.fullstack.biblioteca_proj.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    // Ricerca esatta (come prima)
    List<Book> findByAuthor(String author);
    List<Book> findByName(String name);
    List<Book> findByYear(int year);
    List<Book> findByCategory(String category);
    List<Book> findByDisponibileTrue();
    
    // Aggiunta di metodi per ricerca parziale (case-insensitive)
    @Query("SELECT b FROM Book b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> findByTitleContainingIgnoreCase(@Param("title") String title);
    
    @Query("SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<Book> findByAuthorContainingIgnoreCase(@Param("author") String author);

}