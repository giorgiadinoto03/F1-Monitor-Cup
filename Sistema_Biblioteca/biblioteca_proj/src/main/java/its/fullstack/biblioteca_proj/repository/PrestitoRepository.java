package its.fullstack.biblioteca_proj.repository;

import its.fullstack.biblioteca_proj.models.Prestito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestitoRepository extends JpaRepository<Prestito, Long> { // Repository per la gestione dei prestiti
    List<Prestito> findByUserId(Long userId);
    List<Prestito> findByBookId(Long bookId);
    List<Prestito> findByRestituito(boolean restituito);
    List<Prestito> findByUserIdAndRestituito(Long userId, boolean restituito);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Prestito p WHERE p.user.id = :userId AND p.book.id = :bookId AND p.restituito = false")
    boolean existsByUserIdAndBookIdAndNotRestituito(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query("SELECT p FROM Prestito p WHERE p.user.id = :userId AND p.book.id = :bookId AND p.restituito = false")
    Optional<Prestito> findByUserIdAndBookIdAndRestituitoFalse(@Param("userId") Long userId, @Param("bookId") Long bookId);

}