package its.fullstack.biblioteca_proj.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import its.fullstack.biblioteca_proj.controller.AuthController;
import its.fullstack.biblioteca_proj.models.Ruolo;
import its.fullstack.biblioteca_proj.models.User;
import its.fullstack.biblioteca_proj.security.CustomUserDetailsService;
import its.fullstack.biblioteca_proj.security.JwtUtil;
import its.fullstack.biblioteca_proj.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UserDetails userDetails;
    private AuthController.LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User("Mario", "Rossi", 30, "mario@test.com", "password", Ruolo.CLIENT);
        testUser.setId(1L);

        userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("mario@test.com")
                .password("password")
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))
                .build();

        loginRequest = new AuthController.LoginRequest();
        loginRequest.setEmail("mario@test.com");
        loginRequest.setPassword("password");
    }

    @Test
    @WithMockUser
    void login_WhenValidCredentials_ShouldReturnTokenAndUserData() throws Exception {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userDetailsService.loadUserByUsername("mario@test.com")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("generated-jwt-token");
        when(userService.findByEmail("mario@test.com")).thenReturn(Optional.of(testUser));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("mario@test.com"))
                .andExpect(jsonPath("$.role").value("CLIENT"))
                .andExpect(jsonPath("$.token").value("Bearer generated-jwt-token"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername("mario@test.com");
        verify(jwtUtil).generateToken(userDetails);
        verify(userService).findByEmail("mario@test.com");
    }

    @Test
    @WithMockUser
    void login_WhenInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenziali non valide"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(jwtUtil, never()).generateToken(any());
        verify(userService, never()).findByEmail(any());
    }

    @Test
    @WithMockUser
    void login_WhenAdminUser_ShouldReturnAdminRole() throws Exception {
        // Given
        User adminUser = new User("Admin", "User", 35, "admin@test.com", "password", Ruolo.ADMIN);
        adminUser.setId(2L);

        UserDetails adminUserDetails = org.springframework.security.core.userdetails.User.builder()
                .username("admin@test.com")
                .password("password")
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();

        AuthController.LoginRequest adminLoginRequest = new AuthController.LoginRequest();
        adminLoginRequest.setEmail("admin@test.com");
        adminLoginRequest.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userDetailsService.loadUserByUsername("admin@test.com")).thenReturn(adminUserDetails);
        when(jwtUtil.generateToken(adminUserDetails)).thenReturn("admin-jwt-token");
        when(userService.findByEmail("admin@test.com")).thenReturn(Optional.of(adminUser));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@test.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.token").value("Bearer admin-jwt-token"));
    }

    @Test
    @WithMockUser
    void login_WhenMissingEmail_ShouldReturnBadRequest() throws Exception {
        // Given
        AuthController.LoginRequest invalidRequest = new AuthController.LoginRequest();
        invalidRequest.setPassword("password");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void login_WhenMissingPassword_ShouldReturnBadRequest() throws Exception {
        // Given
        AuthController.LoginRequest invalidRequest = new AuthController.LoginRequest();
        invalidRequest.setEmail("mario@test.com");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_WithoutAuthentication_ShouldAllowAccess() throws Exception {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userDetailsService.loadUserByUsername("mario@test.com")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("generated-jwt-token");
        when(userService.findByEmail("mario@test.com")).thenReturn(Optional.of(testUser));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("mario@test.com"))
                .andExpect(jsonPath("$.role").value("CLIENT"))
                .andExpect(jsonPath("$.token").value("Bearer generated-jwt-token"));
    }
}