package its.fullstack.biblioteca_proj.serviceTest;

import its.fullstack.biblioteca_proj.dto.UserDto;
import its.fullstack.biblioteca_proj.exception.EmailAlreadyUsedException;
import its.fullstack.biblioteca_proj.exception.UserNotFoundException;
import its.fullstack.biblioteca_proj.models.Ruolo;
import its.fullstack.biblioteca_proj.models.User;
import its.fullstack.biblioteca_proj.repository.UserRepository;
import its.fullstack.biblioteca_proj.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUser = new User("Mario", "Rossi", 30, "mario@test.com", "encodedPassword", Ruolo.CLIENT);
        testUser.setId(1L);

        testUserDto = new UserDto();
        testUserDto.setName("Mario");
        testUserDto.setSurname("Rossi");
        testUserDto.setAge(30);
        testUserDto.setEmail("mario@test.com");
        testUserDto.setPassword("plainPassword");
        testUserDto.setRole("CLIENT");
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // Given
        List<User> expectedUsers = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // When
        List<User> result = userService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
        verify(userRepository).findAll();
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // When
        User result = userService.findById(userId);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(userRepository).findById(userId);
    }

    @Test
    void findById_WhenUserNotExists_ShouldThrowUserNotFoundException() {
        // Given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void findByEmail_ShouldReturnUserOptional() {
        // Given
        String email = "mario@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.findByEmail(email);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUser.getEmail(), result.get().getEmail());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void createUser_WhenValidData_ShouldCreateUser() {
        // Given
        when(userRepository.existsByEmail(testUserDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(testUserDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.createUser(testUserDto);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(Ruolo.CLIENT, result.getRole());

        verify(userRepository).existsByEmail(testUserDto.getEmail());
        verify(passwordEncoder).encode(testUserDto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WhenEmailAlreadyExists_ShouldThrowEmailAlreadyUsedException() {
        // Given
        when(userRepository.existsByEmail(testUserDto.getEmail())).thenReturn(true);

        // When & Then
        assertThrows(EmailAlreadyUsedException.class,
                () -> userService.createUser(testUserDto));

        verify(userRepository).existsByEmail(testUserDto.getEmail());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WhenRoleIsNull_ShouldDefaultToClient() {
        // Given
        testUserDto.setRole(null);
        when(userRepository.existsByEmail(testUserDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(testUserDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        // When
        User result = userService.createUser(testUserDto);

        // Then
        assertEquals(Ruolo.CLIENT, result.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WhenRoleIsAdmin_ShouldSetAdminRole() {
        // Given
        testUserDto.setRole("ADMIN");
        when(userRepository.existsByEmail(testUserDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(testUserDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        // When
        User result = userService.createUser(testUserDto);

        // Then
        assertEquals(Ruolo.ADMIN, result.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WhenValidData_ShouldUpdateUser() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(testUserDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(testUserDto.getPassword())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.updateUser(userId, testUserDto);

        // Then
        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUser_WhenUserNotExists_ShouldThrowUserNotFoundException() {
        // Given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(userId, testUserDto));

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WhenEmailChangedToExisting_ShouldThrowEmailAlreadyUsedException() {
        // Given
        Long userId = 1L;
        testUser.setEmail("old@test.com");
        testUserDto.setEmail("new@test.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("new@test.com")).thenReturn(true);

        // When & Then
        assertThrows(EmailAlreadyUsedException.class,
                () -> userService.updateUser(userId, testUserDto));

        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmail("new@test.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WhenPasswordIsEmpty_ShouldNotEncodePassword() {
        // Given
        Long userId = 1L;
        testUserDto.setPassword("");
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(testUserDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.updateUser(userId, testUserDto);

        // Then
        assertNotNull(result);
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUser_WhenPasswordIsNull_ShouldNotEncodePassword() {
        // Given
        Long userId = 1L;
        testUserDto.setPassword(null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(testUserDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.updateUser(userId, testUserDto);

        // Then
        assertNotNull(result);
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository).save(testUser);
    }

    @Test
    void deleteUser_WhenUserExistsAndNoActiveLoans_ShouldDeleteUser() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.hasActiveLoans(userId)).thenReturn(false);

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository).findById(userId);
        verify(userRepository).hasActiveLoans(userId);
        verify(userRepository).delete(testUser);
    }

    @Test
    void deleteUser_WhenUserNotExists_ShouldThrowUserNotFoundException() {
        // Given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));

        verify(userRepository).findById(userId);
        verify(userRepository, never()).hasActiveLoans(any());
        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteUser_WhenUserHasActiveLoans_ShouldThrowIllegalStateException() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.hasActiveLoans(userId)).thenReturn(true);

        // When & Then
        assertThrows(IllegalStateException.class, () -> userService.deleteUser(userId));

        verify(userRepository).findById(userId);
        verify(userRepository).hasActiveLoans(userId);
        verify(userRepository, never()).delete(any());
    }
}