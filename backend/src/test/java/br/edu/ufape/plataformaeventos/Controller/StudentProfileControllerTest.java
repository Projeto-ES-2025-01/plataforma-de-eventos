package br.edu.ufape.plataformaeventos.Controller;

import br.edu.ufape.plataformaeventos.controller.StudentProfileController;
import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.service.EventService;
import br.edu.ufape.plataformaeventos.service.StudentProfileService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentProfileControllerTest {

    @Mock
    private StudentProfileService studentProfileService;

    @Mock
    private EventService eventService;

    @InjectMocks
    private StudentProfileController controller;

    private StudentProfileDTO sampleDTO;
    private StudentProfile sampleStudent;
    private Event sampleEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleDTO = new StudentProfileDTO();
        sampleDTO.setCpf("12345678900");

        sampleStudent = new StudentProfile();
        sampleStudent.setId(1L);
        sampleStudent.setCpf("12345678900");

        sampleEvent = new Event();
        sampleEvent.setId(1L);
    }

    @Test
    void testGetStudentProfile_Found() {
        when(studentProfileService.getStudentProfile("test@example.com")).thenReturn(sampleDTO);

        ResponseEntity<StudentProfileDTO> response = controller.getStudentProfile("test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleDTO, response.getBody());
    }

    @Test
    void testGetStudentProfile_NotFound() {
        when(studentProfileService.getStudentProfile("test@example.com")).thenReturn(null);

        ResponseEntity<StudentProfileDTO> response = controller.getStudentProfile("test@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testEditStudentProfile_Success() {
        ResponseEntity<String> response = controller.editStudentProfile(sampleDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario editado com sucesso.", response.getBody());
        verify(studentProfileService, times(1)).updateStudentProfile(sampleDTO);
    }

    @Test
    void testEditStudentProfile_BadRequest() {
        sampleDTO.setCpf(null);

        ResponseEntity<String> response = controller.editStudentProfile(sampleDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteStudentProfile_Success() {
        doNothing().when(studentProfileService).deleteStudentProfile("test@example.com");

        ResponseEntity<Void> response = controller.deleteStudentProfile("test@example.com");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteStudentProfile_NotFound() {
        doThrow(EntityNotFoundException.class).when(studentProfileService).deleteStudentProfile("test@example.com");

        ResponseEntity<Void> response = controller.deleteStudentProfile("test@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testJoinEvent_Success() {
        when(eventService.findById(1L)).thenReturn(sampleEvent);
        when(studentProfileService.findByCPF("12345678900")).thenReturn(sampleStudent);

        ResponseEntity<String> response = controller.joinEvent(1L, sampleDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Participante adicionado ao evento com sucesso", response.getBody());
        verify(eventService, times(1)).addParticipantToEvent(sampleEvent, sampleStudent);
    }

    @Test
    void testJoinEvent_EventNotFound() {
        when(eventService.findById(1L)).thenReturn(null);

        ResponseEntity<String> response = controller.joinEvent(1L, sampleDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Evento não encontrado", response.getBody());
    }

    @Test
    void testJoinEvent_StudentNotFound() {
        when(eventService.findById(1L)).thenReturn(sampleEvent);
        when(studentProfileService.findByCPF("12345678900")).thenReturn(null);

        ResponseEntity<String> response = controller.joinEvent(1L, sampleDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Estudante não encontrado", response.getBody());
    }

    @Test
    void testLeaveEvent_Success() {
        when(eventService.findById(1L)).thenReturn(sampleEvent);
        when(studentProfileService.findByCPF("12345678900")).thenReturn(sampleStudent);

        ResponseEntity<String> response = controller.leaveEvent(1L, sampleDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Participante removido do evento com sucesso", response.getBody());
        verify(eventService, times(1)).removeParticipantFromEvent(1L, sampleStudent.getId());
    }

    @Test
    void testGetAllStudentProfiles_WithData() {
        List<StudentProfileDTO> profiles = new ArrayList<>();
        profiles.add(sampleDTO);

        when(studentProfileService.getAllStudentProfiles()).thenReturn(profiles);

        ResponseEntity<List<StudentProfileDTO>> response = controller.getAllStudentProfiles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profiles, response.getBody());
    }

    @Test
    void testGetAllStudentProfiles_Empty() {
        when(studentProfileService.getAllStudentProfiles()).thenReturn(new ArrayList<>());

        ResponseEntity<List<StudentProfileDTO>> response = controller.getAllStudentProfiles();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testLeaveEvent_EventNotFound() {
        // Simula evento não encontrado
        when(eventService.findById(1L)).thenReturn(null);

        ResponseEntity<String> response = controller.leaveEvent(1L, sampleDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Evento não encontrado", response.getBody());
    }

    @Test
    void testLeaveEvent_StudentNotFound() {
        // Evento encontrado
        when(eventService.findById(1L)).thenReturn(sampleEvent);
        // Estudante não encontrado
        when(studentProfileService.findByCPF("12345678900")).thenReturn(null);

        ResponseEntity<String> response = controller.leaveEvent(1L, sampleDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Estudante não encontrado", response.getBody());
    }

    @Test
    void testEditStudentProfile_CpfEmpty() {
        sampleDTO.setCpf("");

        ResponseEntity<String> response = controller.editStudentProfile(sampleDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(studentProfileService, never()).updateStudentProfile(any());
    }

}
