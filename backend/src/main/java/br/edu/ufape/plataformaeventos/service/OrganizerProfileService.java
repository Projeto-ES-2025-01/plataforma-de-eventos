package br.edu.ufape.plataformaeventos.service;

import org.springframework.stereotype.Service;

import br.edu.ufape.plataformaeventos.dto.UserDTO;
import br.edu.ufape.plataformaeventos.model.OrganizerProfile;
import br.edu.ufape.plataformaeventos.model.User;
import br.edu.ufape.plataformaeventos.repository.OrganizerProfileRepository;
import br.edu.ufape.plataformaeventos.repository.UserRepository;
import br.edu.ufape.plataformaeventos.util.UserRole;
import jakarta.transaction.Transactional;

@Service
public class OrganizerProfileService {

    
    private UserRepository userRepository;
    private OrganizerProfileRepository organizerProfileRepository;

    public OrganizerProfileService(UserRepository userRepository,
    OrganizerProfileRepository organizerProfileRepository){
        this.userRepository = userRepository;
        this.organizerProfileRepository = organizerProfileRepository;
    }

    @Transactional
    public OrganizerProfile createOrganizerProfile(UserDTO userDTO) {
        User user = new User(userDTO.getName(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                UserRole.ORGANIZER);
        user = userRepository.save(user);

        OrganizerProfile organizerProfile = new OrganizerProfile();
        organizerProfile.setUser(user);
        return organizerProfileRepository.save(organizerProfile);
    }
}
