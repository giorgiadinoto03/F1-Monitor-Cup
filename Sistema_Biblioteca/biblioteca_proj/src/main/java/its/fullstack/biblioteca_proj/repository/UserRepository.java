package its.fullstack.biblioteca_proj.repository;

import its.fullstack.biblioteca_proj.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    /**
     * Verifica se un utente ha prestiti attivi
     * @param userId ID dell'utente da verificare
     * @return true se l'utente ha prestiti attivi, false altrimenti
     */
    @Query("SELECT COUNT(p) > 0 FROM Prestito p WHERE p.user.id = :userId AND p.restituito = false")
    boolean hasActiveLoans(@Param("userId") Long userId);
}