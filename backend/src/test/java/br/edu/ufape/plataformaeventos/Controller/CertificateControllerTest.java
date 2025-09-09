package br.edu.ufape.plataformaeventos.Controller;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.ufape.plataformaeventos.controller.CertificateController;
import br.edu.ufape.plataformaeventos.model.Certificate;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.service.CertificateService;
import br.edu.ufape.plataformaeventos.service.EventService;
import br.edu.ufape.plataformaeventos.service.StudentProfileService;

public class CertificateControllerTest {

    @Mock
    private CertificateService certificateService;

    @Mock
    private EventService eventService;

    @Mock
    private StudentProfileService studentProfileService;

    @InjectMocks
    private CertificateController controller;

    private Certificate sampleCertificate;
    private Event sampleEvent;
    private StudentProfile sampleStudent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleCertificate = new Certificate();
        sampleCertificate.setId(1L);
        sampleCertificate.setCertificateCode("ABC123");

        sampleEvent = new Event();
        sampleEvent.setId(1L);

        sampleStudent = new StudentProfile();
        sampleStudent.setId(1L);
    }

    @Test
    void testSendCertificate_Success() {
        when(eventService.findById(1L)).thenReturn(sampleEvent);
        when(studentProfileService.findByEmail("test@email.com")).thenReturn(sampleStudent);
        when(certificateService.sendCertificate(sampleEvent, sampleStudent)).thenReturn(sampleCertificate);

        Certificate result = controller.sendCertificate(1L, "test@email.com");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(certificateService, times(1)).sendCertificate(sampleEvent, sampleStudent);
    }

    @Test
    void testSendCertificatesForEvent() {
        when(certificateService.sendCertificatesForEvent(1L)).thenReturn(Collections.singletonList(sampleCertificate));

        ResponseEntity<List<Certificate>> response = controller.sendCertificatesForEvent(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
    }

    @Test
    void testGetByParticipantAndEventId() {
        when(eventService.findById(1L)).thenReturn(sampleEvent);
        when(studentProfileService.findByEmail("student@email.com")).thenReturn(sampleStudent);
        when(certificateService.getIdCertificate(sampleEvent, sampleStudent)).thenReturn(99L);

        Long id = controller.getByParticipantAndEventId(1L, "student@email.com");

        assertEquals(99L, id);
    }

    @Test
    void testGetCertificatePdf() {
        byte[] fakePdf = "FAKEPDF".getBytes();

        when(certificateService.findById(1L)).thenReturn(sampleCertificate);
        when(certificateService.generateCertificatePDF(sampleCertificate)).thenReturn(fakePdf);

        ResponseEntity<byte[]> response = controller.getCertificatePdf(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(fakePdf, response.getBody());
        assertEquals("application/pdf", response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    void testVerifyCertificate_Found() {
        when(certificateService.findByCertificateCode("ABC123")).thenReturn(sampleCertificate);

        ResponseEntity<Certificate> response = controller.verifyCertificate("ABC123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleCertificate, response.getBody());
    }

    @Test
    void testVerifyCertificate_NotFound() {
        when(certificateService.findByCertificateCode("NOTFOUND")).thenReturn(null);

        ResponseEntity<Certificate> response = controller.verifyCertificate("NOTFOUND");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
