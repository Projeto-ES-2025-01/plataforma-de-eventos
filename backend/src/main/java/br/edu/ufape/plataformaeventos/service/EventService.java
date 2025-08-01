package br.edu.ufape.plataformaeventos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ufape.plataformaeventos.dto.EventDTO;
import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.OrganizerProfile;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.repository.EventRepository;
import br.edu.ufape.plataformaeventos.repository.OrganizerProfileRepository;
import br.edu.ufape.plataformaeventos.repository.StudentProfileRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private OrganizerProfileRepository organizerProfileRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    public Event createEvent(EventDTO eventDTO) {
        Event entity = new Event();
        this.updateEventProperties(eventDTO, entity); 
        this.eventRepository.save(entity);
        return entity;
    }


    public Event updateEvent(Long idEvent, EventDTO eventDTO) {
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

    public List<StudentProfileDTO> findParticipantsByName(Long EventId, String name) {
        Event event = eventRepository.findById(EventId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado!"));

        Set<StudentProfile> participants = event.getParticipants();    

        return participants.stream()
            .filter(student -> student.getFullName().toLowerCase().contains(name.toLowerCase().trim()))
        .map(StudentProfile::toDTO)
        .collect(Collectors.toList());    
    }

    public StudentProfileDTO findParticipantByCpf(Long eventId, String cpf) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado!"));
        
        StudentProfile participant = studentProfileRepository.findByCpf(cpf);
        if (participant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participante não encontrado!");
        }

        if (!event.getParticipants().contains(participant)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participante não está inscrito neste evento!");
        }
        return participant.toDTO();    
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
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
