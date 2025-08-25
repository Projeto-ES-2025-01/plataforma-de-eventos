package br.edu.ufape.plataformaeventos.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.dto.UserDTO;
import br.edu.ufape.plataformaeventos.dto.UserRegistrationDTO;
import br.edu.ufape.plataformaeventos.model.User;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.model.OrganizerProfile;
import br.edu.ufape.plataformaeventos.repository.UserRepository;
import br.edu.ufape.plataformaeventos.service.AuthService;
import br.edu.ufape.plataformaeventos.service.StudentProfileService;
import br.edu.ufape.plataformaeventos.service.OrganizerProfileService;
import br.edu.ufape.plataformaeventos.util.UserRole;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentProfileService studentProfileService;

    @Mock
    private OrganizerProfileService organizerProfileService;

    @InjectMocks
    private AuthService authService;

    private UserDTO userDTO;
    private StudentProfileDTO studentProfileDTO;
    private UserRegistrationDTO userRegistrationDTO;
    private User user;
    private StudentProfile studentProfile;
    private OrganizerProfile organizerProfile;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO("Test User", "test@example.com", "password123", UserRole.STUDENT);
        
        studentProfileDTO = new StudentProfileDTO();
        studentProfileDTO.setFullName("Test Student");
        studentProfileDTO.setCpf("12345678900");

        userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setUserDTO(userDTO);
        userRegistrationDTO.setStudentProfileDTO(studentProfileDTO);

        user = new User("Test User", "test@example.com", "password123", UserRole.STUDENT);
        
        studentProfile = new StudentProfile();
        studentProfile.setId(1L);
        studentProfile.setCpf("12345678900");
        organizerProfile = new OrganizerProfile();
        organizerProfile.setId(2L);
    }
    
 
    @Test
    void deveCarregarUsuarioPorEmailComSucesso() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        UserDetails foundUser = authService.loadUserByUsername("test@example.com");

        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }


    @Test
    void deveRegistrarEstudanteComSucesso() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        
        when(studentProfileService.createStudentProfile(any(UserDTO.class), any(StudentProfileDTO.class)))
            .thenReturn(studentProfile);

        Object result = authService.register(userRegistrationDTO);

        assertNotNull(result);
        assertEquals(studentProfile, result);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(studentProfileService, times(1)).createStudentProfile(any(UserDTO.class), any(StudentProfileDTO.class));
    }

    @Test
    void deveRegistrarOrganizadorComSucesso() {
        UserDTO organizerUserDTO = new UserDTO("Organizer User", "organizer@example.com", "pass", UserRole.ORGANIZER);
        UserRegistrationDTO organizerRegistrationDTO = new UserRegistrationDTO();
        organizerRegistrationDTO.setUserDTO(organizerUserDTO);
        organizerRegistrationDTO.setStudentProfileDTO(null);

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        
        when(organizerProfileService.createOrganizerProfile(any(UserDTO.class)))
            .thenReturn(organizerProfile);

        Object result = authService.register(organizerRegistrationDTO);

        assertNotNull(result);
        assertEquals(organizerProfile, result);
        verify(userRepository, times(1)).findByEmail("organizer@example.com");
        verify(organizerProfileService, times(1)).createOrganizerProfile(any(UserDTO.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExistir() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        assertThrows(ResponseStatusException.class, () -> {
            authService.register(userRegistrationDTO);
        }, "Email já existe!");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(studentProfileService, never()).createStudentProfile(any(), any());
    }

}