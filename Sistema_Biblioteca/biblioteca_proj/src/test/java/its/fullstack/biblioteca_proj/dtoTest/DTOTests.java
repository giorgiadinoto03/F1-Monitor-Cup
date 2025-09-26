package its.fullstack.biblioteca_proj.dtoTest;

import its.fullstack.biblioteca_proj.dto.*;
import its.fullstack.biblioteca_proj.models.Book;
import its.fullstack.biblioteca_proj.models.Prestito;
import its.fullstack.biblioteca_proj.models.Ruolo;
import its.fullstack.biblioteca_proj.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DTOTests {

    private Book testBook;
    private User testUser;
    private Prestito testPrestito;

    @BeforeEach
    void setUp() {
        testBook = new Book("Test Book", "Test Author", "Fiction", 2023);
        testBook.setId(1L);
        testBook.setDisponibile(true);

        testUser = new User("Mario", "Rossi", 30, "mario@test.com", "password", Ruolo.CLIENT);
        testUser.setId(1L);

        testPrestito = new Prestito();
        testPrestito.setId(1L);
        testPrestito.setUser(testUser);
        testPrestito.setBook(testBook);
        testPrestito.setDataPrestito(LocalDate.now());
        testPrestito.setDataRestituzione(LocalDate.now().plusDays(30));
        testPrestito.setRestituito(false);
    }

    @Test
    void bookDTO_WhenCreatedFromBook_ShouldMapAllFields() {
        // When
        BookDTO bookDTO = new BookDTO(testBook);

        // Then
        assertEquals(testBook.getId(), bookDTO.getId());
        assertEquals(testBook.getName(), bookDTO.getName());
        assertEquals(testBook.getAuthor(), bookDTO.getAuthor());
        assertEquals(testBook.getCategory(), bookDTO.getCategory());
        assertEquals(testBook.getYear(), bookDTO.getYear());
        assertEquals(testBook.isDisponibile(), bookDTO.isDisponibile());
    }

    @Test
    void bookMapper_toDTO_ShouldConvertBookToDTO() {
        // Given
        BookMapper bookMapper = new BookMapper();

        // When
        BookDTO result = bookMapper.toDTO(testBook);

        // Then
        assertNotNull(result);
        assertEquals(testBook.getId(), result.getId());
        assertEquals(testBook.getName(), result.getName());
        assertEquals(testBook.getAuthor(), result.getAuthor());
    }

    @Test
    void bookMapper_toDTOList_ShouldConvertListOfBooksToListOfDTOs() {
        // Given
        BookMapper bookMapper = new BookMapper();
        Book anotherBook = new Book("Another Book", "Another Author", "Science", 2022);
        anotherBook.setId(2L);
        List<Book> books = Arrays.asList(testBook, anotherBook);

        // When
        List<BookDTO> result = bookMapper.toDTOList(books);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testBook.getId(), result.get(0).getId());
        assertEquals(anotherBook.getId(), result.get(1).getId());
    }

    @Test
    void prestitoDTO_WhenCreatedFromPrestito_ShouldMapAllFields() {
        // When
        PrestitoDTO prestitoDTO = new PrestitoDTO(testPrestito);

        // Then
        assertEquals(testPrestito.getId(), prestitoDTO.getId());
        assertEquals(testPrestito.getDataPrestito(), prestitoDTO.getDataPrestito());
        assertEquals(testPrestito.getDataRestituzione(), prestitoDTO.getDataRestituzione());
        assertEquals(testPrestito.isRestituito(), prestitoDTO.isRestituito());
        assertEquals(testUser.getId(), prestitoDTO.getUserId());
        assertEquals("Mario Rossi", prestitoDTO.getUserName());
        assertEquals(testBook.getId(), prestitoDTO.getBookId());
        assertEquals(testBook.getName(), prestitoDTO.getBookTitle());
    }

    @Test
    void prestitoDTO_WhenUserIsNull_ShouldHandleGracefully() {
        // Given
        testPrestito.setUser(null);

        // When
        PrestitoDTO prestitoDTO = new PrestitoDTO(testPrestito);

        // Then
        assertNull(prestitoDTO.getUserId());
        assertNull(prestitoDTO.getUserName());
        // Altri campi dovrebbero essere mappati normalmente
        assertEquals(testPrestito.getId(), prestitoDTO.getId());
        assertEquals(testBook.getId(), prestitoDTO.getBookId());
    }

    @Test
    void prestitoDTO_WhenBookIsNull_ShouldHandleGracefully() {
        // Given
        testPrestito.setBook(null);

        // When
        PrestitoDTO prestitoDTO = new PrestitoDTO(testPrestito);

        // Then
        assertNull(prestitoDTO.getBookId());
        assertNull(prestitoDTO.getBookTitle());
        // Altri campi dovrebbero essere mappati normalmente
        assertEquals(testPrestito.getId(), prestitoDTO.getId());
        assertEquals(testUser.getId(), prestitoDTO.getUserId());
    }

    @Test
    void prestitoDTO_NoArgsConstructor_ShouldCreateEmptyObject() {
        // When
        PrestitoDTO prestitoDTO = new PrestitoDTO();

        // Then
        assertNull(prestitoDTO.getId());
        assertNull(prestitoDTO.getUserId());
        assertNull(prestitoDTO.getBookId());
        assertFalse(prestitoDTO.isRestituito());
    }

    @Test
    void userDto_SetRole_WhenNullProvided_ShouldDefaultToClient() {
        // Given
        UserDto userDto = new UserDto();

        // When
        userDto.setRole(null);

        // Then
        assertEquals("CLIENT", userDto.getRole());
    }

    @Test
    void userDto_SetRole_WhenValidRoleProvided_ShouldSetRole() {
        // Given
        UserDto userDto = new UserDto();

        // When
        userDto.setRole("ADMIN");

        // Then
        assertEquals("ADMIN", userDto.getRole());
    }

    @Test
    void userDto_DefaultConstructor_ShouldSetDefaultValues() {
        // When
        UserDto userDto = new UserDto();

        // Then
        assertEquals("CLIENT", userDto.getRole());
    }

    @Test
    void userResponseDto_SettersAndGetters_ShouldWorkCorrectly() {
        // Given
        UserResponseDto userResponseDto = new UserResponseDto();

        // When
        userResponseDto.setId(1L);
        userResponseDto.setEmail("test@test.com");
        userResponseDto.setRole("CLIENT");

        // Then
        assertEquals(1L, userResponseDto.getId());
        assertEquals("test@test.com", userResponseDto.getEmail());
        assertEquals("CLIENT", userResponseDto.getRole());
    }

    @Test
    void loginResponse_Constructor_ShouldSetAllFields() {
        // When
        LoginResponse loginResponse = new LoginResponse("test@test.com", "CLIENT", "Bearer token123");

        // Then
        assertEquals("test@test.com", loginResponse.getEmail());
        assertEquals("CLIENT", loginResponse.getRole());
        assertEquals("Bearer token123", loginResponse.getToken());
    }
}