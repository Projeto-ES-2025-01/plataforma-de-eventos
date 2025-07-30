package br.edu.ufape.plataformaeventos.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.plataformaeventos.model.Event;


@Repository
public interface EventRepository extends JpaRepository<Event, Long>,EventRepositoryCustom {

    List<Event> findByNameIgnoreCase(String name);
    
    
}