package br.edu.ufape.plataformaeventos.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import br.edu.ufape.plataformaeventos.model.Certificate;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.repository.CertificateRepository;
import br.edu.ufape.plataformaeventos.repository.EventRepository;

@Service
public class CertificateService {

    private CertificateRepository certificateRepository;
    private EventRepository eventRepository;

    public CertificateService(CertificateRepository certificateRepository, EventRepository eventRepository) {
        this.certificateRepository = certificateRepository;
        this.eventRepository = eventRepository;
    }

    public Certificate sendCertificate(Event event, StudentProfile participant) {
        String code = generateUniqueCode();
        Certificate certificate = new Certificate(event, participant, LocalDate.now(), code);
        return certificateRepository.save(certificate);
    }

    public Certificate findById(Long certificateId) {
        return certificateRepository.findById(certificateId)
            .orElseThrow(() -> new RuntimeException("Certificado não encontrado"));
    }

    public List<Certificate> sendCertificatesForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        
        List<Certificate> certificatesSended = new ArrayList<>();
        for (StudentProfile participant : event.getConfirmedParticipants()) {
            Certificate certificate = sendCertificate(event, participant);
            certificatesSended.add(certificate);
        }
        return certificatesSended;

    }

    public byte[] generateCertificatePDF(Certificate certificate) {
        String html = buildCertificateHTML(certificate);
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF do certificado", e);
        }
    }
 
    private String generateUniqueCode() {
        return UUID.randomUUID().toString();
    }

    private String buildCertificateHTML(Certificate certificate) {
         String template = """
        <html>
        <body>
        <h1>Certificado</h1>
        <p>Certificamos que %s participou do evento %s em %s.</p>
        <p>Código: %s</p>
        </body>
        </html>
        """;
        return String.format(template,
                certificate.getParticipant().getFullName(),
                certificate.getEvent().getName(),
                certificate.getEvent().getDate().toString(),
                certificate.getCertificateCode());
    }
}
