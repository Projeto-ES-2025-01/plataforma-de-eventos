package br.edu.ufape.plataformaeventos.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.plataformaeventos.dto.EventDTO;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.service.EventService;
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
}
