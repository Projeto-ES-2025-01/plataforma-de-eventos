package br.edu.ufape.plataformaeventos.Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.edu.ufape.plataformaeventos.dto.EventDTO;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.OrganizerProfile;
import br.edu.ufape.plataformaeventos.model.StudentProfile;

class EventTest {

    private Event event;
    private OrganizerProfile organizer;
    private StudentProfile participante1;
    private StudentProfile participante2;

    @BeforeEach
    void setUp() {
        organizer = mock(OrganizerProfile.class);
        when(organizer.getId()).thenReturn(1L);
        
        participante1 = mock(StudentProfile.class);
        participante2 = mock(StudentProfile.class);
        
        event = new Event(
            "Teste",
            LocalDate.of(2024, 12, 15),
            LocalTime.of(9, 0),
            "ufape",
            "Renato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato Renato",
            organizer
        );
    }

    @Test
    void testConstrutorVazio() {
        Event defaultEvent = new Event();
        assertNotNull(defaultEvent);
        assertNull(defaultEvent.getName());
        assertNull(defaultEvent.getDate());
        assertNull(defaultEvent.getTime());
        assertNull(defaultEvent.getLocation());
        assertNull(defaultEvent.getDescription());
        assertNull(defaultEvent.getOrganizer());
        assertNotNull(defaultEvent.getParticipants());
        assertTrue(defaultEvent.getParticipants().isEmpty());
    }

    @Test
    void testConstrutor() {
        assertEquals("Teste", event.getName());
        assertEquals(LocalDate.of(2024, 12, 15), event.getDate());
        assertEquals(LocalTime.of(9, 0), event.getTime());
        assertEquals("ufape", event.getLocation());
        assertEquals("Renato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato Renato", event.getDescription());
        assertEquals(organizer, event.getOrganizer());
        assertNotNull(event.getParticipants());
        assertTrue(event.getParticipants().isEmpty());
    }

    @Test
    void testGetAndSet() {
        event.setId(100L);
        assertEquals(100L, event.getId());

        event.setName("Teste");
        assertEquals("Teste", event.getName());

        LocalDate newDate = LocalDate.of(2025, 1, 20);
        event.setDate(newDate);
        assertEquals(newDate, event.getDate());

        LocalTime newTime = LocalTime.of(14, 30);
        event.setTime(newTime);
        assertEquals(newTime, event.getTime());

        event.setLocation("Lugar");
        assertEquals("Lugar", event.getLocation());

        event.setDescription("Renato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato Renato");
        assertEquals("Renato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato Renato", event.getDescription());

        OrganizerProfile newOrganizer = mock(OrganizerProfile.class);
        event.setOrganizer(newOrganizer);
        assertEquals(newOrganizer, event.getOrganizer());
    }

    @Test
    void testGetIdOrganizer() {
        assertEquals(1L, event.getIdOrganizer());
    }

    @Test
    void testAddParticipant() {
        // Initially empty
        assertTrue(event.getParticipants().isEmpty());

        // Add first participant
        event.addParticipant(participante1);
        assertEquals(1, event.getParticipants().size());
        assertTrue(event.getParticipants().contains(participante1));

        // Add second participant
        event.addParticipant(participante2);
        assertEquals(2, event.getParticipants().size());
        assertTrue(event.getParticipants().contains(participante1));
        assertTrue(event.getParticipants().contains(participante2));

        // Add duplicate participant (should not add again)
        event.addParticipant(participante1);
        assertEquals(2, event.getParticipants().size());
    }

    @Test
    void testSetParticipants() {
        Set<StudentProfile> Participantes = new HashSet<>();
        Participantes.add(participante1);
        Participantes.add(participante2);

        event.setParticipants(Participantes);
        
        assertEquals(2, event.getParticipants().size());
        assertTrue(event.getParticipants().contains(participante1));
        assertTrue(event.getParticipants().contains(participante2));
        assertSame(Participantes, event.getParticipants());
    }

    @Test
    void testEventToEventDTO() {
        EventDTO eventDTO = event.eventToEventDTO();

        assertNotNull(eventDTO);
        assertEquals("Teste", eventDTO.getName());
        assertEquals(LocalDate.of(2024, 12, 15), eventDTO.getDate());
        assertEquals(LocalTime.of(9, 0), eventDTO.getTime());
        assertEquals("ufape", eventDTO.getLocation());
        assertEquals("Renato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato RenatoRenato Renato", eventDTO.getDescription());
        assertEquals(1L, eventDTO.getIdOrganizer());
    }

}
