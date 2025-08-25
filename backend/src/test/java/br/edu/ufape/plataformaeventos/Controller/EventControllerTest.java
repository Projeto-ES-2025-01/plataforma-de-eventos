package br.edu.ufape.plataformaeventos.Controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.ufape.plataformaeventos.controller.EventController;
import br.edu.ufape.plataformaeventos.dto.EventDTO;
import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.service.EventService;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private Event event;
    private EventDTO eventDTO;
    private StudentProfileDTO studentProfileDTO;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setName("Tech Conference 2024");
        
        eventDTO = new EventDTO();
        eventDTO.setName("Tech Conference 2024");
        
        studentProfileDTO = new StudentProfileDTO();
        studentProfileDTO.setFullName("John Doe");
        studentProfileDTO.setCpf("12345678900");
    }

    
    @Test
    void testCreateEvent() {
        when(eventService.createEvent(any(EventDTO.class))).thenReturn(event);
        
        ResponseEntity<Event> resposta = eventController.createEvent(eventDTO);
        
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(event, resposta.getBody());
        verify(eventService, times(1)).createEvent(eventDTO);
    }

   
    @Test
    void testUpdateEvent() {
        when(eventService.updateEvent(eq(1L), any(EventDTO.class))).thenReturn(event);
        
        ResponseEntity<Event> resposta = eventController.updateEvent(1L, eventDTO);
        
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(event, resposta.getBody());
        verify(eventService, times(1)).updateEvent(1L, eventDTO);
    }

    @Test
    void deleteEvent() {
        doNothing().when(eventService).deleteEvent(1L);
        
        ResponseEntity<String> resposta = eventController.deleteEvent(1L);
        
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("Evento deletado com sucesso!", resposta.getBody());
        verify(eventService, times(1)).deleteEvent(1L);
    }

    @Test
    void testGetEventDetails() {
        when(eventService.getEventDetails(1L)).thenReturn(event);
        
        Event result = eventController.getEventDetails(1L);
        
        assertEquals(event.getId(), result.getId());
        verify(eventService, times(1)).getEventDetails(1L);
    }

    // Teste para getAllEvents que tenha eventos
    @Test
    void testGetAllEventsComEventos() {
        List<Event> events = Arrays.asList(event);
        when(eventService.getAllEvents()).thenReturn(events);
        
        ResponseEntity<List<Event>> responde = eventController.getAllEvents();
        
        assertEquals(HttpStatus.OK, responde.getStatusCode());
        assertEquals(1, responde.getBody().size());
        assertEquals(event, responde.getBody().get(0));
        verify(eventService, times(1)).getAllEvents();
    }

    // Teste para getAllEvents vazio
    @Test
    void testGetAllEventsSemEventos() {
        when(eventService.getAllEvents()).thenReturn(Collections.emptyList());
        
        ResponseEntity<List<Event>> resposta = eventController.getAllEvents();
        
        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        assertNull(resposta.getBody());
        verify(eventService, times(1)).getAllEvents();
    }

    // Teste para getAllParticipantByEvent que tenha participantes
    @Test
    void testGetAllParticipantByEvent() {
        List<StudentProfileDTO> participants = Arrays.asList(studentProfileDTO);
        when(eventService.getAllParticipantByEvent(1L)).thenReturn(participants);
        
        ResponseEntity<List<StudentProfileDTO>> resposta = eventController.getAllParticipantByEvent(1L);
        
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(1, resposta.getBody().size());
        assertEquals(studentProfileDTO, resposta.getBody().get(0));
        verify(eventService, times(1)).getAllParticipantByEvent(1L);
    }

    // Teste para getAllParticipantByEvent que não tenha participantes
    @Test
    void testGetAllParticipantByEventVazio() {
        when(eventService.getAllParticipantByEvent(1L)).thenReturn(Collections.emptyList());
        
        ResponseEntity<List<StudentProfileDTO>> resposta = eventController.getAllParticipantByEvent(1L);
        
        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        assertNull(resposta.getBody());
        verify(eventService, times(1)).getAllParticipantByEvent(1L);
    }

    // Teste para getAllParticipantByEvent evento não encontrado
    @Test
    void testGetAllParticipantByEventNaoEncontrado() {
        when(eventService.getAllParticipantByEvent(1L)).thenThrow(new EntityNotFoundException("Evento não encontrado"));
        
        ResponseEntity<List<StudentProfileDTO>> encontrou = eventController.getAllParticipantByEvent(1L);
        
        assertEquals(HttpStatus.NOT_FOUND, encontrou.getStatusCode());
        verify(eventService, times(1)).getAllParticipantByEvent(1L);
    }

}