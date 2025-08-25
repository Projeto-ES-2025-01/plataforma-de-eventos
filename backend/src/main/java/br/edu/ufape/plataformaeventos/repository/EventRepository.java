package br.edu.ufape.plataformaeventos.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.OrganizerProfile;
import br.edu.ufape.plataformaeventos.model.StudentProfile;


@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

    List<Event> findByNameIgnoreCase(String name);

    Event findById(long id);

    @Override
    List<Event> findAll();
    
    List<Event> findByParticipantsContaining(StudentProfile student);
    List<Event> findByOrganizer(OrganizerProfile organizer);

}