package br.edu.ufape.plataformaeventos.service;

import java.time.LocalDate;
import java.util.List;


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


    public Event updateLocation(Long idEvent, EventDTO eventDTO) {
        Event entity = eventRepository.findById(idEvent)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
        "Evento não encontrado!"));

        this.updateEventProperties(eventDTO, entity);
        eventRepository.save(entity);
        return entity;
    }
    
    public Event getEventDetails(Long idEvent) {
        return this.eventRepository.findById(idEvent)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
        "Evento não encontrada!"));
    }

    public void deleteEvent(Long idEvent ) {
        if (idEvent == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do Evento é obrigatório!");
        }
        Event entity = eventRepository.findById(idEvent)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado!"));

        eventRepository.delete(entity);
    }
    
    public List<Event> findByName(String name) {
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
