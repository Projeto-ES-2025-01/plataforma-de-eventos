package br.edu.ufape.plataformaeventos.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.plataformaeventos.dto.EventDTO;
import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/event")
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService){
        this.eventService = eventService;
    }
    
    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@RequestBody @Valid EventDTO eventoDTO) {
    Event evento = eventService.createEvent(eventoDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(evento);
    }

    @PutMapping("/update/{idEvent}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long idEvent, 
     @Valid @RequestBody EventDTO eventDTO) {
        Event updatedEvent = eventService.updateEvent(idEvent, eventDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedEvent);
    }

    @DeleteMapping("/delete/{idEvent}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long idEvent) {
        eventService.deleteEvent(idEvent);
        return ResponseEntity.status(HttpStatus.OK).body("Evento deletado com sucesso!");
    }

    @GetMapping("/get/{idEvent}")
    public Event getEventDetails(@PathVariable Long idEvent) {
        return eventService.getEventDetails(idEvent);
    }

    @GetMapping("/allEvents")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        if (events.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @GetMapping("/AllParticipants/{idEvent}")
    public ResponseEntity<List<StudentProfileDTO>> getAllParticipantByEvent(@PathVariable Long idEvent) {
        try {
            List<StudentProfileDTO> participants = eventService.getAllParticipantByEvent(idEvent);
            return participants.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(participants);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
