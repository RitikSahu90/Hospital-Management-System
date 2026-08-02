package hospital.management.backend.service;

import hospital.management.backend.dto.request.LoginRequest;
import hospital.management.backend.dto.request.RegisterRequest;
import hospital.management.backend.dto.response.AuthResponse;
import hospital.management.backend.entity.Role;
import hospital.management.backend.entity.User;
import hospital.management.backend.repository.RoleRepository;
import hospital.management.backend.repository.UserRepository;
import hospital.management.backend.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin123");

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("new@example.com");
        registerRequest.setPassword("pass123");
    }

    @Test
    void shouldLoginSuccessfully() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("admin");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken("admin")).thenReturn("token-123");

        AuthResponse response = authService.login(loginRequest);

        assertThat(response.getToken()).isEqualTo("token-123");
        assertThat(response.getUsername()).isEqualTo("admin");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken("admin");
    }

    @Test
    void shouldThrowWhenLoginFails() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void shouldRegisterNewUserWhenRoleExists() {
        Role role = Role.builder().id(1L).name("PATIENT").build();

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(roleRepository.findByName("PATIENT")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("pass123")).thenReturn("encoded-pass");
        when(jwtUtil.generateToken("newuser")).thenReturn("token-456");

        AuthResponse response = authService.register(registerRequest);

        assertThat(response.getToken()).isEqualTo("token-456");
        assertThat(response.getUsername()).isEqualTo("newuser");
        verify(userRepository).save(any(User.class));
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void shouldRegisterNewUserAndCreateRoleWhenRoleMissing() {
        Role savedRole = Role.builder().id(1L).name("PATIENT").build();

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(roleRepository.findByName("PATIENT")).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);
        when(passwordEncoder.encode("pass123")).thenReturn("encoded-pass");
        when(jwtUtil.generateToken("newuser")).thenReturn("token-789");

        AuthResponse response = authService.register(registerRequest);

        assertThat(response.getToken()).isEqualTo("token-789");
        assertThat(response.getUsername()).isEqualTo("newuser");
        verify(roleRepository).save(any(Role.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowWhenUsernameAlreadyExists() {
        when(userRepository.existsByUsername("newuser")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authService.register(registerRequest));

        assertThat(exception.getMessage()).isEqualTo("Username already exists");
        verify(userRepository, never()).save(any(User.class));
        verify(jwtUtil, never()).generateToken(anyString());
    }
}