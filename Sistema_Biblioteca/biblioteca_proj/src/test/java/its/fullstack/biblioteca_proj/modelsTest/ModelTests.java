package its.fullstack.biblioteca_proj.modelsTest;

import its.fullstack.biblioteca_proj.models.Book;
import its.fullstack.biblioteca_proj.models.Prestito;
import its.fullstack.biblioteca_proj.models.Ruolo;
import its.fullstack.biblioteca_proj.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ModelTests {

    private User testUser;
    private Book testBook;
    private Prestito testPrestito;

    @BeforeEach
    void setUp() {
        testUser = new User("Mario", "Rossi", 30, "mario@test.com", "password", Ruolo.CLIENT);
        testUser.setId(1L);

        testBook = new Book("Test Book", "Test Author", "Fiction", 2023);
        testBook.setId(1L);

        testPrestito = new Prestito();
        testPrestito.setId(1L);
        testPrestito.setUser(testUser);
        testPrestito.setBook(testBook);
        testPrestito.setDataPrestito(LocalDate.now());
        testPrestito.setDataRestituzione(LocalDate.now().plusDays(30));
        testPrestito.setRestituito(false);
    }

    // === BOOK TESTS ===
    @Test
    void book_DefaultConstructor_ShouldSetDefaultValues() {
        // When
        Book book = new Book();

        // Then
        assertTrue(book.isDisponibile());
        assertNotNull(book.getPrestiti());
        assertTrue(book.getPrestiti().isEmpty());
    }

    @Test
    void book_ParameterizedConstructor_ShouldSetAllFields() {
        // When
        Book book = new Book("Title", "Author", "Category", 2023);

        // Then
        assertEquals("Title", book.getName());
        assertEquals("Author", book.getAuthor());
        assertEquals("Category", book.getCategory());
        assertEquals(2023, book.getYear());
        assertTrue(book.isDisponibile());
    }

    @Test
    void book_Equals_WhenSameId_ShouldReturnTrue() {
        // Given
        Book book1 = new Book();
        book1.setId(1L);
        Book book2 = new Book();
        book2.setId(1L);

        // When & Then
        assertEquals(book1, book2);
        assertEquals(book1.hashCode(), book2.hashCode());
    }

    @Test
    void book_Equals_WhenDifferentId_ShouldReturnFalse() {
        // Given
        Book book1 = new Book();
        book1.setId(1L);
        Book book2 = new Book();
        book2.setId(2L);

        // When & Then
        assertNotEquals(book1, book2);
    }

    @Test
    void book_Equals_WhenIdIsNull_ShouldUseSuperEquals() {
        // Given
        Book book1 = new Book();
        Book book2 = new Book();

        // When & Then
        assertEquals(book1, book1); // Same reference
        assertNotEquals(book1, book2); // Different objects
    }

    @Test
    void book_ToString_ShouldContainAllFields() {
        // When
        String result = testBook.toString();

        // Then
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name='Test Book'"));
        assertTrue(result.contains("author='Test Author'"));
        assertTrue(result.contains("category='Fiction'"));
        assertTrue(result.contains("year=2023"));
        assertTrue(result.contains("disponibile=true"));
    }

    // === USER TESTS ===
    @Test
    void user_DefaultConstructor_ShouldSetDefaultValues() {
        // When
        User user = new User();

        // Then
        assertEquals(Ruolo.CLIENT, user.getRole());
        assertNotNull(user.getPrestiti());
        assertTrue(user.getPrestiti().isEmpty());
    }

    @Test
    void user_ParameterizedConstructor_ShouldSetAllFields() {
        // When
        User user = new User("Mario", "Rossi", 30, "mario@test.com", "password", Ruolo.ADMIN);

        // Then
        assertEquals("Mario", user.getName());
        assertEquals("Rossi", user.getSurname());
        assertEquals(30, user.getAge());
        assertEquals("mario@test.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(Ruolo.ADMIN, user.getRole());
    }

    @Test
    void user_GetNomeCompleto_ShouldReturnFullName() {
        // When
        String nomeCompleto = testUser.getNomeCompleto();

        // Then
        assertEquals("Mario Rossi", nomeCompleto);
    }

    @Test
    void user_HasPrestitiAttivi_WhenNoActiveLoans_ShouldReturnFalse() {
        // Given
        Prestito prestitoRestituito = new Prestito();
        prestitoRestituito.setRestituito(true);
        testUser.setPrestiti(new ArrayList<>());
        testUser.getPrestiti().add(prestitoRestituito);

        // When
        boolean hasActiveLoans = testUser.haPrestitiAttivi();

        // Then
        assertFalse(hasActiveLoans);
    }

    @Test
    void user_HasPrestitiAttivi_WhenHasActiveLoans_ShouldReturnTrue() {
        // Given
        Prestito prestitoAttivo = new Prestito();
        prestitoAttivo.setRestituito(false);
        testUser.setPrestiti(new ArrayList<>());
        testUser.getPrestiti().add(prestitoAttivo);

        // When
        boolean hasActiveLoans = testUser.haPrestitiAttivi();

        // Then
        assertTrue(hasActiveLoans);
    }

    @Test
    void user_ToString_ShouldContainMainFields() {
        // When
        String result = testUser.toString();

        // Then
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name='Mario'"));
        assertTrue(result.contains("surname='Rossi'"));
        assertTrue(result.contains("email='mario@test.com'"));
        assertTrue(result.contains("role=CLIENT"));
        assertFalse(result.contains("password")); // Password should not be in toString
    }

    // === PRESTITO TESTS ===
    @Test
    void prestito_DefaultConstructor_ShouldSetDefaultValues() {
        // When
        Prestito prestito = new Prestito();

        // Then
        assertFalse(prestito.isRestituito());
    }

    @Test
    void prestito_ParameterizedConstructor_ShouldSetAllFields() {
        // Given
        LocalDate dataPrestito = LocalDate.now();
        LocalDate dataRestituzione = LocalDate.now().plusDays(30);

        // When
        Prestito prestito = new Prestito(testUser, testBook, dataPrestito, dataRestituzione);

        // Then
        assertEquals(testUser, prestito.getUser());
        assertEquals(testBook, prestito.getBook());
        assertEquals(dataPrestito, prestito.getDataPrestito());
        assertEquals(dataRestituzione, prestito.getDataRestituzione());
        assertFalse(prestito.isRestituito());
    }

    @Test
    void prestito_SetRestituito_WhenTrueAndDataRestitutionNull_ShouldSetCurrentDate() {
        // Given
        testPrestito.setDataRestituzione(null);

        // When
        testPrestito.setRestituito(true);

        // Then
        assertTrue(testPrestito.isRestituito());
        assertEquals(LocalDate.now(), testPrestito.getDataRestituzione());
    }

    @Test
    void prestito_SetRestituito_WhenTrueAndDataRestitutionExists_ShouldNotChangeDate() {
        // Given
        LocalDate originalDate = LocalDate.of(2023, 12, 15);
        testPrestito.setDataRestituzione(originalDate);

        // When
        testPrestito.setRestituito(true);

        // Then
        assertTrue(testPrestito.isRestituito());
        assertEquals(originalDate, testPrestito.getDataRestituzione());
    }

    @Test
    void prestito_SetRestituito_WhenFalse_ShouldNotChangeDate() {
        // Given
        LocalDate originalDate = LocalDate.of(2023, 12, 15);
        testPrestito.setDataRestituzione(originalDate);

        // When
        testPrestito.setRestituito(false);

        // Then
        assertFalse(testPrestito.isRestituito());
        assertEquals(originalDate, testPrestito.getDataRestituzione());
    }

    // === RUOLO ENUM TESTS ===
    @Test
    void ruolo_FromString_WhenValidString_ShouldReturnCorrectEnum() {
        // When & Then
        assertEquals(Ruolo.ADMIN, Ruolo.fromString("ADMIN"));
        assertEquals(Ruolo.ADMIN, Ruolo.fromString("admin"));
        assertEquals(Ruolo.CLIENT, Ruolo.fromString("CLIENT"));
        assertEquals(Ruolo.CLIENT, Ruolo.fromString("client"));
    }

    @Test
    void ruolo_FromString_WhenInvalidString_ShouldReturnClient() {
        // When & Then
        assertEquals(Ruolo.CLIENT, Ruolo.fromString("INVALID"));
        assertEquals(Ruolo.CLIENT, Ruolo.fromString(""));
        assertEquals(Ruolo.CLIENT, Ruolo.fromString("USER"));
    }

    @Test
    void ruolo_FromString_WhenNull_ShouldReturnClient() {
        // When & Then
        assertEquals(Ruolo.CLIENT, Ruolo.fromString(null));
    }

    @Test
    void ruolo_IsAdmin_ShouldReturnCorrectBoolean() {
        // When & Then
        assertTrue(Ruolo.ADMIN.isAdmin());
        assertFalse(Ruolo.CLIENT.isAdmin());
    }

    @Test
    void ruolo_IsClient_ShouldReturnCorrectBoolean() {
        // When & Then
        assertFalse(Ruolo.ADMIN.isClient());
        assertTrue(Ruolo.CLIENT.isClient());
    }

    @Test
    void ruolo_GetRuolo_ShouldReturnStringValue() {
        // When & Then
        assertEquals("ADMIN", Ruolo.ADMIN.getRuolo());
        assertEquals("CLIENT", Ruolo.CLIENT.getRuolo());
    }

    @Test
    void ruolo_ToString_ShouldReturnRuoloValue() {
        // When & Then
        assertEquals("ADMIN", Ruolo.ADMIN.toString());
        assertEquals("CLIENT", Ruolo.CLIENT.toString());
    }
}