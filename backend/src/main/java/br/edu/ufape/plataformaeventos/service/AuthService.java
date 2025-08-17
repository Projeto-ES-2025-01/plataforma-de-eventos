package br.edu.ufape.plataformaeventos.service;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.dto.UserDTO;
import br.edu.ufape.plataformaeventos.dto.UserRegistrationDTO;
import br.edu.ufape.plataformaeventos.repository.UserRepository;

@Service
public class AuthService implements UserDetailsService {
    
    private UserRepository userRepository;
    private StudentProfileService studentProfileService;
    private OrganizerProfileService organizerProfileService;

    public AuthService(UserRepository userRepository, StudentProfileService studentProfileService, OrganizerProfileService organizerProfileService){
        this.userRepository = userRepository;
        this.organizerProfileService = organizerProfileService;
        this.studentProfileService= studentProfileService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username);
    }

    public Object register(UserRegistrationDTO userRegistrationDTO) {
        UserDTO userDTO = userRegistrationDTO.getUserDTO();
        StudentProfileDTO studentProfileDTO = userRegistrationDTO.getStudentProfileDTO();

        
        UserDetails userDetails = this.userRepository.findByEmail(userDTO.getEmail());

        if (userDetails != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já existe!");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userDTO.getPassword());
        userDTO.setPassword(encryptedPassword);

        switch (userDTO.getRole()) {
            case STUDENT -> {
                if (studentProfileDTO == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Perfil de estudante é obrigatório!");
                }
                return studentProfileService.createStudentProfile(userDTO, studentProfileDTO);
            }
                case ORGANIZER -> {
                    return organizerProfileService.createOrganizerProfile(userDTO);
            }
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de usuário inválido!");
        }
    }
}