package br.edu.ufape.plataformaeventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.plataformaeventos.model.OrganizerProfile;

@Repository
public interface OrganizerProfileRepository extends JpaRepository<OrganizerProfile, Long> {

    OrganizerProfile findByUserEmail(String email);
    
}
