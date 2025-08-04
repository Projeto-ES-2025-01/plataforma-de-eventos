package br.edu.ufape.plataformaeventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import br.edu.ufape.plataformaeventos.model.User;
import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    UserDetails findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);
}
