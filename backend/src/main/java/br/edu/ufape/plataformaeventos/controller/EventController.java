package br.edu.ufape.plataformaeventos.controller;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.plataformaeventos.dto.EventDTO;
import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;
    
    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@RequestBody @Valid EventDTO eventoDTO) {
    Event evento = eventService.createEvent(eventoDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(evento);
    }

    @PutMapping("/update/{idEvent}")
    public ResponseEntity<Event> updateLocation(@PathVariable Long idEvent, 
     @Valid @RequestBody EventDTO eventDTO) {
        Event updatedEvent = eventService.updateLocation(idEvent, eventDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedEvent);
    }

    @DeleteMapping("/delete/{idEvent}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long idEvent) {
        eventService.deleteEvent(idEvent);
        return ResponseEntity.status(HttpStatus.OK).body("Evento deletado com sucesso!");
    }

    @GetMapping("/get/{idEvent}")
    public Event getLocationDetails(@PathVariable Long idEvent) {
        return eventService.getEventDetails(idEvent);
    }

    @GetMapping("/search")
    public List<Event> searchEvent(@RequestParam(required = false) String name){
        if (name != null) {
            return eventService.findByName(name);
        } 
        return Collections.emptyList();
    }

    @GetMapping("/search/date") 
    public List<Event> searchByDateBetween(@RequestParam(required = false) LocalDate minDate, 
        @RequestParam(required = false) LocalDate maxDate) {
        return eventService.findByDateBetween(minDate, maxDate);
    }

    @GetMapping("/search/participants/{eventId}")
    public ResponseEntity<?> searchParticipantsByName(
        @PathVariable Long eventId,
        @RequestParam String name) {

            try {
                List<StudentProfileDTO> participants = eventService.findParticipantsByName(eventId, name);
                return participants.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(participants);
            } catch (EntityNotFoundException e) {
                return ResponseEntity.notFound().build();
            }
        }

}
