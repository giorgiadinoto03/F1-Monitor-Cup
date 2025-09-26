package its.fullstack.biblioteca_proj.dto;

import its.fullstack.biblioteca_proj.models.Prestito;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PrestitoDTO {
    private Long id;
    private LocalDate dataPrestito;
    private LocalDate dataRestituzione;
    private boolean restituito;

    @NotNull(message = "L'ID utente non può essere nullo")
    private Long userId;

    private String userName;

    @NotNull(message = "L'ID del libro non può essere nullo")
    private Long bookId;

    private String bookTitle;


    public PrestitoDTO(Prestito prestito) {
        this.id = prestito.getId();
        this.dataPrestito = prestito.getDataPrestito();
        this.dataRestituzione = prestito.getDataRestituzione();
        this.restituito = prestito.isRestituito();

        if (prestito.getUser() != null) { //Se il prestito non ha un utente, non viene restituito
            this.userId = prestito.getUser().getId();
            this.userName = prestito.getUser().getName() + " " + prestito.getUser().getSurname();
        }

        if (prestito.getBook() != null) {   //Se il prestito non ha un libro, non viene restituito
            this.bookId = prestito.getBook().getId();
            this.bookTitle = prestito.getBook().getName();
        }
    }

}