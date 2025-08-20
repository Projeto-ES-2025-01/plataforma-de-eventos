package br.edu.ufape.plataformaeventos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.dto.UserDTO;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.model.User;
import br.edu.ufape.plataformaeventos.repository.EventRepository;
import br.edu.ufape.plataformaeventos.repository.StudentProfileRepository;
import br.edu.ufape.plataformaeventos.repository.UserRepository;
import br.edu.ufape.plataformaeventos.util.UserRole;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class StudentProfileService {

    private UserRepository userRepository;
    private StudentProfileRepository studentProfileRepository;
    private EventRepository eventRepository;

    public StudentProfileService(UserRepository userRepository, StudentProfileRepository studentProfileRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.eventRepository = eventRepository;
    }


    @Transactional
    public StudentProfile createStudentProfile(UserDTO userDTO, StudentProfileDTO studentProfileDTO) {
        User user = new User(userDTO.getName(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                UserRole.STUDENT);
        user = userRepository.save(user);

        StudentProfile studentProfile = mapProfileToStudent(studentProfileDTO);
        studentProfile.setUser(user);
        return studentProfileRepository.save(studentProfile);
    }

    private StudentProfile mapProfileToStudent(StudentProfileDTO studentProfileDTO) {
        StudentProfile studentProfile = new StudentProfile();
        studentProfile.setFullName(studentProfileDTO.getFullName());
        studentProfile.setCpf(studentProfileDTO.getCpf());
        studentProfile.setBirthDate(studentProfileDTO.getBirthDate());
        studentProfile.setPhoneNumber(studentProfileDTO.getPhoneNumber());
        studentProfile.setDegreeProgram(studentProfileDTO.getDegreeProgram());
        studentProfile.setCurrentPeriod(studentProfileDTO.getCurrentPeriod());
        return studentProfile;
    }

    public StudentProfileDTO getStudentProfile(String email) {
        StudentProfile studentProfile = studentProfileRepository.findByUserEmail(email);
        
        if (studentProfile == null){
            return null;
        }
        else {
            return studentProfile.toDTO();
        }
    }

    public void updateStudentProfile(StudentProfileDTO studentProfileDTO){
        StudentProfile studentProfile = studentProfileRepository.findByCpf(studentProfileDTO.getCpf());

        if (studentProfile != null) {
            studentProfile.setFullName(studentProfileDTO.getFullName());
            studentProfile.setBirthDate(studentProfileDTO.getBirthDate());
            studentProfile.setPhoneNumber(studentProfileDTO.getPhoneNumber());
            studentProfile.setDegreeProgram(studentProfileDTO.getDegreeProgram());
            studentProfile.setCurrentPeriod(studentProfileDTO.getCurrentPeriod());
            studentProfileRepository.save(studentProfile);
        }
    }   

    @Transactional
    public void deleteStudentProfile(String email) {
        StudentProfile studentProfile = studentProfileRepository.findByUserEmail(email);
        if (studentProfile == null) {
            throw new EntityNotFoundException("Perfil de Estudante não encontrado com esse email: " + email);
        }
        List<Event> events = eventRepository.findByParticipantsContaining(studentProfile);
        for (Event event : events) {
            event.getParticipants().remove(studentProfile);
            eventRepository.save(event);
        }
        studentProfileRepository.delete(studentProfile);
        userRepository.deleteByEmail(email);
    }

    public StudentProfile findByCPF(String cpf){
        return studentProfileRepository.findByCpf(cpf);
    }

    public List<StudentProfileDTO> getAllStudentProfiles() {
        return studentProfileRepository.findAll().stream()
                .map(StudentProfile::toDTO)
                .toList();
    }
}
