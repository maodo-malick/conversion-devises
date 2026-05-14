package conversion_devises.service;

import conversion_devises.dto.request.LoginRequest;
import conversion_devises.dto.request.RegisterRequest;
import conversion_devises.dto.response.AuthReponse;
import conversion_devises.entity.Role;
import conversion_devises.entity.User;
import conversion_devises.repository.UserRepository;
import conversion_devises.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    // ---- REGISTER ----

    @Test
    void register_shouldReturnToken_whenEmailNotUsed() {
        // Préparer
        RegisterRequest request = new RegisterRequest();
        request.setUsername("Moussa");
        request.setEmail("moussa@gmail.com");
        request.setPassword("123456");

        User savedUser = User.builder()
                .id(1L)
                .username("Moussa")
                .email("moussa@gmail.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.existsByEmail("moussa@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("fakeToken");

        // Exécuter
        AuthReponse response = authService.register(request);

        // Vérifier
        assertNotNull(response);
        assertEquals("fakeToken", response.getToken());
        assertEquals("moussa@gmail.com", response.getEmail());
        assertEquals(Role.USER, response.getRole());
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyUsed() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("moussa@gmail.com");

        when(userRepository.existsByEmail("moussa@gmail.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    // ---- LOGIN ----

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setEmail("moussa@gmail.com");
        request.setPassword("123456");

        User user = User.builder()
                .id(1L)
                .username("Moussa")
                .email("moussa@gmail.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail("moussa@gmail.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("fakeToken");

        AuthReponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("fakeToken", response.getToken());
        assertEquals("moussa@gmail.com", response.getEmail());
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@gmail.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("unknown@gmail.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(request));
    }
}