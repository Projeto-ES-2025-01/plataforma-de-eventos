package br.edu.ufape.plataformaeventos.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.assertThrows;


import br.edu.ufape.plataformaeventos.controller.AuthController;
import br.edu.ufape.plataformaeventos.dto.AuthDTO;
import br.edu.ufape.plataformaeventos.dto.LoginResponseDTO;
import br.edu.ufape.plataformaeventos.dto.UserDTO;
import br.edu.ufape.plataformaeventos.dto.UserRegistrationDTO;
import br.edu.ufape.plataformaeventos.model.User;
import br.edu.ufape.plataformaeventos.security.TokenService;
import br.edu.ufape.plataformaeventos.service.AuthService;
import br.edu.ufape.plataformaeventos.util.UserRole;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthController authController;

    private AuthDTO authDTO;
    private UserRegistrationDTO userRegistrationDTO;
    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        authDTO = new AuthDTO("test@example.com", "password123");
        
        userDTO = new UserDTO();
        userDTO.setEmail("student@example.com");
        userDTO.setPassword("password456");

        userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setUserDTO(userDTO);
        user = new User("Test User", "test@example.com", "password123", UserRole.STUDENT);
    }

    @Test
    void testLoginComSucesso() {
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenService.generateToken(any(User.class))).thenReturn("fake-jwt-token");

        ResponseEntity<LoginResponseDTO> resposta = authController.login(authDTO);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("fake-jwt-token", resposta.getBody().getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(1)).generateToken(any(User.class));
    }

@Test
void deveRetornarUnauthorizedEmLoginInvalido() throws Exception {
    AuthDTO authDTO = new AuthDTO("invalid@example.com", "wrong-password");
    
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Credenciais inválidas"));
    assertThrows(BadCredentialsException.class, () -> {

        authController.login(authDTO);
    });

    verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
}

    @Test
    void testRegisterStudentComSucesso() {
        Object mockResult = new Object();
        when(authService.register(any(UserRegistrationDTO.class))).thenReturn(mockResult);

        ResponseEntity<Object> resposta = authController.registerStudent(userRegistrationDTO);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(mockResult, resposta.getBody());
        assertEquals(UserRole.STUDENT, userRegistrationDTO.getUserDTO().getRole());
        verify(authService, times(1)).register(any(UserRegistrationDTO.class));
    }

    @Test
    void testRegisterOrganizerComSucesso() {
        Object mockResult = new Object();
        when(authService.register(any(UserRegistrationDTO.class))).thenReturn(mockResult);

        UserDTO organizerUserDTO = new UserDTO();
        organizerUserDTO.setEmail("organizer@example.com");
        organizerUserDTO.setPassword("organizer_pass");
        
        ResponseEntity<Object> resposta = authController.registerOrganizer(organizerUserDTO);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(mockResult, resposta.getBody());
        verify(authService, times(1)).register(any(UserRegistrationDTO.class));
    }
}