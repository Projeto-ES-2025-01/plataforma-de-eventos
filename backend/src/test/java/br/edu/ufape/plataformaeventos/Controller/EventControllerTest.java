package br.edu.ufape.plataformaeventos.Controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
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

    @Test
    void testSearchEvent_WithName() {
        List<Event> events = Arrays.asList(event);
        when(eventService.findByName("Tech")).thenReturn(events);
        
        List<Event> result = eventController.searchEvent("Tech");
        
        assertEquals(1, result.size());
        assertEquals(event, result.get(0));
        verify(eventService, times(1)).findByName("Tech");
    }

    // Teste para searchEvent - Sem nome
    @Test
    void testSearchEvent_WithoutName() {
        List<Event> result = eventController.searchEvent(null);
        
        assertTrue(result.isEmpty());
        verify(eventService, never()).findByName(anyString());
    }

    // Teste para searchByDateBetween - Com datas
    @Test
    void testSearchByDateBetween_WithDates() {
        LocalDate minDate = LocalDate.of(2024, 1, 1);
        LocalDate maxDate = LocalDate.of(2024, 12, 31);
        List<Event> events = Arrays.asList(event);
        
        when(eventService.findByDateBetween(minDate, maxDate)).thenReturn(events);
        
        List<Event> result = eventController.searchByDateBetween(minDate, maxDate);
        
        assertEquals(1, result.size());
        assertEquals(event, result.get(0));
        verify(eventService, times(1)).findByDateBetween(minDate, maxDate);
    }

    // Teste para searchByDateBetween - Sem datas
    @Test
    void testSearchByDateBetween_WithoutDates() {
        List<Event> result = eventController.searchByDateBetween(null, null);
        
        assertNotNull(result);
        verify(eventService, times(1)).findByDateBetween(null, null);
    }

    // Teste para searchParticipantsByName - Sucesso com participantes
    @Test
    void testSearchParticipantsByName_SuccessWithParticipants() {
        List<StudentProfileDTO> participants = Arrays.asList(studentProfileDTO);
        when(eventService.findParticipantsByName(1L, "John")).thenReturn(participants);
        
        ResponseEntity<List<StudentProfileDTO>> resposta = eventController.searchParticipantsByName(1L, "John");
        
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(1, resposta.getBody().size());
        assertEquals(studentProfileDTO, resposta.getBody().get(0));
        verify(eventService, times(1)).findParticipantsByName(1L, "John");
    }

    // Teste para searchParticipantsByName - Sucesso sem participantes
    @Test
    void testSearchParticipantsByName_SuccessNoContent() {
        when(eventService.findParticipantsByName(1L, "John")).thenReturn(Collections.emptyList());
        
        ResponseEntity<List<StudentProfileDTO>> resposta = eventController.searchParticipantsByName(1L, "John");
        
        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        assertNull(resposta.getBody());
        verify(eventService, times(1)).findParticipantsByName(1L, "John");
    }

    // Teste para searchParticipantsByName - Evento não encontrado
    @Test
    void testSearchParticipantsByName_EventNotFound() {
        when(eventService.findParticipantsByName(1L, "John")).thenThrow(new EntityNotFoundException("Event not found"));
        
        ResponseEntity<List<StudentProfileDTO>> resposta = eventController.searchParticipantsByName(1L, "John");
        
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(eventService, times(1)).findParticipantsByName(1L, "John");
    }

    // Teste para searchParticipantByCpf 
    @Test
    void testSearchParticipantByCpf() {
        when(eventService.findParticipantByCpf(1L, "12345678900")).thenReturn(studentProfileDTO);
        
        ResponseEntity<StudentProfileDTO> resposta = eventController.searchParticipantByCpf(1L, "12345678900");
        
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(studentProfileDTO, resposta.getBody());
        verify(eventService, times(1)).findParticipantByCpf(1L, "12345678900");
    }

    // Teste para searchParticipantByCpf participante não encontrado
    @Test
    void testSearchParticipantByCpfNaoEncontrado() {
        when(eventService.findParticipantByCpf(1L, "12345678900")).thenThrow(new EntityNotFoundException("Participante nao encontrado"));
        
        ResponseEntity<StudentProfileDTO> resposta = eventController.searchParticipantByCpf(1L, "12345678900");
        
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(eventService, times(1)).findParticipantByCpf(1L, "12345678900");
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