package br.edu.ufape.plataformaeventos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.dto.UserDTO;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.model.User;
import br.edu.ufape.plataformaeventos.repository.StudentProfileRepository;
import br.edu.ufape.plataformaeventos.repository.UserRepository;
import br.edu.ufape.plataformaeventos.util.UserRole;
import jakarta.transaction.Transactional;

@Service
public class StudentProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

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

}
