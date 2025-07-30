package br.edu.ufape.plataformaeventos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ufape.plataformaeventos.dto.EventDTO;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.OrganizerProfile;
import br.edu.ufape.plataformaeventos.repository.EventRepository;
import br.edu.ufape.plataformaeventos.repository.OrganizerProfileRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private OrganizerProfileRepository organizerProfileRepository;

    public Event createEvent(EventDTO eventDTO) {
        Event entity = new Event();
        this.updateEventProperties(eventDTO, entity); 
        this.eventRepository.save(entity);
        return entity;
    }


    public Event updateLocation(String nameEvent, EventDTO eventDTO) {
        Event entity = eventRepository.findByName(nameEvent)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
        "Evento não encontrado!"));

        this.updateEventProperties(eventDTO, entity);
        eventRepository.save(entity);
        return entity;
    }
    
    public Event getEventDetails(String nameEvent) {
        return this.eventRepository.findByName(nameEvent)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
        "Evento não encontrada!"));
    }

    public void deleteLocation(String nameEvent) {
        if (nameEvent == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do Evento é obrigatório!");
        }
        Event entity = eventRepository.findByName(nameEvent)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado!"));

        eventRepository.delete(entity);
    }

    public Optional<Event> findByName(String name) {
        return eventRepository.findByName(name);
    }
    
    public List<Event> findByNameIgnoreCase(String name) {
        return eventRepository.findByNameIgnoreCase(name);
    }

    public List<Event> findByDateBetween(LocalDate miDate,LocalDate maxDate){
        return eventRepository.findByDateBetween(maxDate, miDate);
    }



    

private void updateEventProperties(EventDTO eventDTO, Event entity) {
    entity.setName(eventDTO.getName());
    entity.setDate(eventDTO.getDate());
    entity.setTime(eventDTO.getTime());
    entity.setLocation(eventDTO.getLocation());
    entity.setDescription(eventDTO.getDescription());

    OrganizerProfile organizer = organizerProfileRepository.findById(eventDTO.getId())
        .orElseThrow(() -> new RuntimeException("Organizador não encontrado"));

    entity.setOrganizer(organizer);
}
    
   
}
