package br.edu.ufape.plataformaeventos.service;


import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ufape.plataformaeventos.dto.EventDTO;
import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.OrganizerProfile;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.model.User;
import br.edu.ufape.plataformaeventos.repository.EventRepository;
import br.edu.ufape.plataformaeventos.repository.OrganizerProfileRepository;
import br.edu.ufape.plataformaeventos.repository.StudentProfileRepository;
import br.edu.ufape.plataformaeventos.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class EventService {
    
    private static final String MENSAGEM_EVENTO_NAO_ENCONTRADO =  "Evento não encontrado!";

    private EventRepository eventRepository;
    private StudentProfileRepository studentRepository;
    private UserRepository userRepository;
    private OrganizerProfileRepository organizerProfileRepository;


    public EventService(EventRepository eventRepository,StudentProfileRepository studentRepository, UserRepository userRepository,OrganizerProfileRepository organizerProfileRepository){
        this.eventRepository = eventRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.organizerProfileRepository = organizerProfileRepository;
    }

    public Event createEvent(EventDTO eventDTO) {
        Event entity = new Event();
        this.updateEventProperties(eventDTO, entity); 
        this.eventRepository.save(entity);
        return entity;
    }



    public Event updateEvent(Long idEvent, EventDTO eventDTO) {
        Event entity = eventRepository.findById(idEvent)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
        MENSAGEM_EVENTO_NAO_ENCONTRADO));

        this.updateEventProperties(eventDTO, entity);
        eventRepository.save(entity);
        return entity;
    }
    
    public Event getEventDetails(Long idEvent) {
        return this.eventRepository.findById(idEvent)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
        MENSAGEM_EVENTO_NAO_ENCONTRADO));
    }

    public void deleteEvent(Long idEvent ) {
        if (idEvent == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do Evento é obrigatório!");
        }
        Event entity = eventRepository.findById(idEvent)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MENSAGEM_EVENTO_NAO_ENCONTRADO));

        eventRepository.delete(entity);
    }
    
    @Transactional
    public void addParticipantToEvent(Event event, StudentProfile participant) {
        event.addParticipant(participant);
    }


    @Transactional
    public void removeParticipantFromEvent(Long eventId, Long studentId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(MENSAGEM_EVENTO_NAO_ENCONTRADO));
        StudentProfile student = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotFoundException("Student não encontrado!"));

        event.getParticipants().remove(student);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<StudentProfileDTO> getAllParticipantByEvent (Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MENSAGEM_EVENTO_NAO_ENCONTRADO));

        Set<StudentProfile> participants = event.getParticipants();
        if (participants == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não há inscritos.");
        }
        return participants.stream()
        .map(StudentProfile::toDTO)
        .toList();
    }

private void updateEventProperties(EventDTO eventDTO, Event entity) {
    entity.setName(eventDTO.getName());
    entity.setDate(eventDTO.getDate());
    entity.setTime(eventDTO.getTime());
    entity.setLocation(eventDTO.getLocation());
    entity.setDescription(eventDTO.getDescription());

    User user = userRepository.findById(eventDTO.getIdOrganizer())
        .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
    OrganizerProfile organizer = organizerProfileRepository.findByUserEmail(user.getUsername())
        .orElseThrow(() -> new RuntimeException("Organizador não encontrado"));
       

    entity.setOrganizer(organizer);
}

    public Event findById(Long eventId) {
        return eventRepository.findById(eventId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MENSAGEM_EVENTO_NAO_ENCONTRADO));
    
   
}

public List<StudentProfileDTO> getAllConfirmedParticipantByEvent (Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MENSAGEM_EVENTO_NAO_ENCONTRADO));

        Set<StudentProfile> participants = event.getConfirmedParticipants();
        if (participants == null || participants.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não há confirmados.");
        }
        return participants.stream()
        .map(StudentProfile::toDTO)
        .toList();
    }
}
