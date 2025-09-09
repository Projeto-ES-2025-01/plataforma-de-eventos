package br.edu.ufape.plataformaeventos.Service;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import br.edu.ufape.plataformaeventos.model.Certificate;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.repository.CertificateRepository;
import br.edu.ufape.plataformaeventos.service.CertificateService;

public class CertificateServiceTest {

    @Mock
    private CertificateRepository certificateRepository;

    @InjectMocks
    private CertificateService certificateService;

    private Event sampleEvent;
    private StudentProfile sampleStudent;
    private Certificate sampleCertificate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleEvent = new Event();
        sampleEvent.setId(1L);
        sampleEvent.setName("Evento Teste");
        sampleEvent.setDate(LocalDate.of(2025, 1, 1));

        sampleStudent = new StudentProfile();
        sampleStudent.setId(2L);
        sampleStudent.setFullName("Aluno Teste");

        sampleCertificate = new Certificate(sampleEvent, sampleStudent, LocalDate.now(), "ABC123");
        sampleCertificate.setId(3L);
    }

    @Test
    void testSendCertificate_Success() {
        when(certificateRepository.save(any(Certificate.class))).thenReturn(sampleCertificate);

        Certificate result = certificateService.sendCertificate(sampleEvent, sampleStudent);

        assertNotNull(result);
        assertEquals("ABC123", result.getCertificateCode()); // porque mockamos o retorno
        verify(certificateRepository, times(1)).save(any(Certificate.class));
    }

    @Test
    void testFindById_Found() {
        when(certificateRepository.findById(3L)).thenReturn(Optional.of(sampleCertificate));

        Certificate result = certificateService.findById(3L);

        assertEquals(3L, result.getId());
        assertEquals("ABC123", result.getCertificateCode());
    }

    @Test
    void testFindByParticipantAndEventId() {
        when(certificateRepository.findByParticipantAndEventId(sampleEvent, sampleStudent))
                .thenReturn(sampleCertificate);

        Certificate result = certificateService.findByParticipantAndEventId(sampleEvent, sampleStudent);

        assertEquals(sampleCertificate, result);
    }

    @Test
    void testFindByCertificateCode() {
        when(certificateRepository.findByCertificateCode("ABC123")).thenReturn(sampleCertificate);

        Certificate result = certificateService.findByCertificateCode("ABC123");

        assertEquals(sampleCertificate, result);
    }

    @Test
    void testGenerateCertificatePDF_Success() {
        byte[] pdf = certificateService.generateCertificatePDF(sampleCertificate);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    @Test
    void testGetIdCertificate_Found() {
        when(certificateRepository.findByEventAndParticipant(sampleEvent, sampleStudent))
                .thenReturn(sampleCertificate);

        Long result = certificateService.getIdCertificate(sampleEvent, sampleStudent);

        assertEquals(3L, result);
    }
}
