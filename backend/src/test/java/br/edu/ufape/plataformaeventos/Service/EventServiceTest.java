package br.edu.ufape.plataformaeventos.Service;

import br.edu.ufape.plataformaeventos.dto.EventDTO;
import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.OrganizerProfile;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.model.User;
import br.edu.ufape.plataformaeventos.repository.EventRepository;
import br.edu.ufape.plataformaeventos.repository.OrganizerProfileRepository;
import br.edu.ufape.plataformaeventos.repository.UserRepository;
import br.edu.ufape.plataformaeventos.repository.StudentProfileRepository;
import br.edu.ufape.plataformaeventos.service.EventService;
import br.edu.ufape.plataformaeventos.util.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrganizerProfileRepository organizerProfileRepository;

    @InjectMocks
    private EventService eventService;

    private EventDTO eventDTO;
    private User user;
    private OrganizerProfile organizer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User("teste", "organizador@gmail.com", "Teste123", UserRole.ORGANIZER);
        user.setId(1L);

        eventDTO = new EventDTO();
        eventDTO.setName("Festa");
        eventDTO.setLocation("Recife");
        eventDTO.setDescription("Ano novo");
        eventDTO.setDate(LocalDate.of(2026, 1, 1));
        eventDTO.setTime(LocalTime.of(2, 0));
        eventDTO.setIdOrganizer(1L);

        organizer = new OrganizerProfile();
        organizer.setId(1L);
        organizer.setUser(user);
    }

    @Test
    void testAddParticipantToEvent() {
        Event event = new Event();
        StudentProfile student = new StudentProfile();

        eventService.addParticipantToEvent(event, student);
        assertTrue(event.getParticipants().contains(student));
    }

    /*@Test
    void testFindByName() {
        Event event = new Event();
        event.setName("Festa");
        when(eventRepository.findByNameIgnoreCase("Festa")).thenReturn(Collections.singletonList(event));

        List<Event> events = eventService.findByName("Festa");
        assertEquals(1, events.size());
        assertEquals("Festa", events.get(0).getName());
    }*/

    @Test
    void testDeleteEventSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(organizerProfileRepository.findByUserEmail("organizador@gmail.com"))
                .thenReturn(Optional.of(organizer));

        Event result = eventService.createEvent(eventDTO);
        assertNotNull(result);
        verify(eventRepository, times(1)).save(result);

        eventRepository.deleteById(1L);

        assertNull(eventRepository.findById(1L));

    }

    @Test
    void testDeleteEventNotFound() {
        when(eventRepository.findById(1L)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> eventService.deleteEvent(1L));

        assertEquals("404 NOT_FOUND \"Evento não encontrado!\"", exception.getMessage());
    }

    @Test
    void testCreateEventSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(organizerProfileRepository.findByUserEmail("organizador@gmail.com"))
                .thenReturn(Optional.of(organizer));

        Event result = eventService.createEvent(eventDTO);

        assertNotNull(result);
        assertEquals("Festa", result.getName());
        assertEquals("Recife", result.getLocation());
        assertEquals("Ano novo", result.getDescription());
        assertEquals(organizer, result.getOrganizer());

        verify(eventRepository, times(1)).save(result);
    }

    @Test
    void testGetAllEventsSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(organizerProfileRepository.findByUserEmail("organizador@gmail.com"))
                .thenReturn(Optional.of(organizer));

        Event result = eventService.createEvent(eventDTO);
        assertNotNull(result);

        verify(eventRepository, times(1)).save(result);
        assertNotNull(eventService.getAllEvents());
    }


    @Test
    void testCreateEventOrganizerNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(organizerProfileRepository.findByUserEmail(anyString())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventService.createEvent(eventDTO));

        assertEquals("Organizador não encontrado", exception.getMessage());
    }

    @Test
    void testUpdateEventNotFound() {
        when(eventRepository.findById(99L)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> eventService.updateEvent(99L, eventDTO));

        assertEquals("404 NOT_FOUND \"Evento não encontrado!\"", exception.getMessage());
    }

}
