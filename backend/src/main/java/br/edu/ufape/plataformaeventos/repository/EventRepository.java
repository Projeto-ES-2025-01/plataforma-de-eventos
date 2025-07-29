package br.edu.ufape.plataformaeventos.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.plataformaeventos.model.Event;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByName(String name);

    List<Event> findByNameIgnoreCase(String name);
    
    
}