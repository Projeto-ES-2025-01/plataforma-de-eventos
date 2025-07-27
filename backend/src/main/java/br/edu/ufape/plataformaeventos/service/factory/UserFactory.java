package br.edu.ufape.plataformaeventos.service.factory;

import br.edu.ufape.plataformaeventos.dto.UserDTO;
import br.edu.ufape.plataformaeventos.model.User;

public interface UserFactory {
    User createUser(UserDTO userDTO);
}
