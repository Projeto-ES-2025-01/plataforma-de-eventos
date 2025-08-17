package br.edu.ufape.plataformaeventos.Service;

import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.dto.UserDTO;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.model.User;
import br.edu.ufape.plataformaeventos.repository.EventRepository;
import br.edu.ufape.plataformaeventos.repository.StudentProfileRepository;
import br.edu.ufape.plataformaeventos.repository.UserRepository;
import br.edu.ufape.plataformaeventos.service.StudentProfileService;
import br.edu.ufape.plataformaeventos.util.DegreeProgram;
import br.edu.ufape.plataformaeventos.util.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentProfileRepository studentProfileRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private StudentProfileService studentProfileService;

    private UserDTO userDTO;
    private StudentProfileDTO studentDTO;
    private User user;
    private StudentProfile studentProfile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTO = new UserDTO();
        userDTO.setName("João");
        userDTO.setEmail("joao@email.com");
        userDTO.setPassword("Teste123");

        studentDTO = new StudentProfileDTO();
        studentDTO.setFullName("João Silva");
        studentDTO.setCpf("12345678900");
        studentDTO.setBirthDate(LocalDate.of(2000, 1, 1));
        studentDTO.setPhoneNumber("99999999999");
        studentDTO.setDegreeProgram(DegreeProgram.CIENCIA_DA_COMPUTACAO);
        studentDTO.setCurrentPeriod(5);

        user = new User("João", "joao@email.com", "123456", UserRole.STUDENT);
        user.setId(1L);

        studentProfile = new StudentProfile();
        studentProfile.setId(1L);
        studentProfile.setCpf("12345678900");
        studentProfile.setFullName("João Silva");
        studentProfile.setUser(user);
    }

    @Test
    void testCreateStudentProfile() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(studentProfileRepository.save(any(StudentProfile.class))).thenReturn(studentProfile);

        StudentProfile result = studentProfileService.createStudentProfile(userDTO, studentDTO);

        assertNotNull(result);
        assertEquals("João Silva", result.getFullName());
        assertEquals(user, result.getUser());

        verify(userRepository, times(1)).save(any(User.class));
        verify(studentProfileRepository, times(1)).save(any(StudentProfile.class));
    }

    @Test
    void testGetStudentProfileExists() {
        when(studentProfileRepository.findByUserEmail("joao@email.com")).thenReturn(studentProfile);

        StudentProfileDTO dto = studentProfileService.getStudentProfile("joao@email.com");

        assertNotNull(dto);
        assertEquals("João Silva", dto.getFullName());
    }

    @Test
    void testGetStudentProfileNotExists() {
        when(studentProfileRepository.findByUserEmail("naoexiste@email.com")).thenReturn(null);

        StudentProfileDTO dto = studentProfileService.getStudentProfile("naoexiste@email.com");

        assertNull(dto);
    }

    @Test
    void testUpdateStudentProfile() {
        when(studentProfileRepository.findByCpf("12345678900")).thenReturn(studentProfile);
        when(studentProfileRepository.save(any(StudentProfile.class))).thenReturn(studentProfile);

        studentDTO.setFullName("João Atualizado");
        studentProfileService.updateStudentProfile(studentDTO);

        assertEquals("João Atualizado", studentProfile.getFullName());
        verify(studentProfileRepository, times(1)).save(studentProfile);
    }

    @Test
    void testDeleteStudentProfileSuccess() {
        when(studentProfileRepository.findByUserEmail("joao@email.com")).thenReturn(studentProfile);
        doNothing().when(studentProfileRepository).delete(studentProfile);
        doNothing().when(userRepository).deleteByEmail("joao@email.com");

        assertDoesNotThrow(() -> studentProfileService.deleteStudentProfile("joao@email.com"));

        verify(studentProfileRepository, times(1)).delete(studentProfile);
        verify(userRepository, times(1)).deleteByEmail("joao@email.com");
    }

    @Test
    void testDeleteStudentProfileNotFound() {
        when(studentProfileRepository.findByUserEmail("naoexiste@email.com")).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> studentProfileService.deleteStudentProfile("naoexiste@email.com"));

        assertEquals("Perfil de Estudante não encontrado com esse email: naoexiste@email.com", exception.getMessage());
    }

    @Test
    void testFindByCPF() {
        when(studentProfileRepository.findByCpf("12345678900")).thenReturn(studentProfile);

        StudentProfile result = studentProfileService.findByCPF("12345678900");

        assertEquals(studentProfile, result);
    }

    @Test
    void testGetAllStudentProfiles() {
        when(studentProfileRepository.findAll()).thenReturn(Arrays.asList(studentProfile));

        List<StudentProfileDTO> result = studentProfileService.getAllStudentProfiles();

        assertEquals(1, result.size());
        assertEquals("João Silva", result.get(0).getFullName());
    }
}
