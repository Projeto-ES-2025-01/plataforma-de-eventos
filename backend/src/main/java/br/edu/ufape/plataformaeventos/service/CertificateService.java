package br.edu.ufape.plataformaeventos.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

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
        String code = gerarCodigoUnico();
        Certificate certificate = new Certificate(event, participant, LocalDate.now(), code);
        return certificateRepository.save(certificate);
    }

    public List<Certificate> sendCertificatesForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        
        List<Certificate> certificatesSended = new ArrayList<>();
        for (StudentProfile participant : event.getParticipants()) {
            Certificate certificate = sendCertificate(event, participant);
            certificatesSended.add(certificate);
        }
        return certificatesSended;
    
    }

    private String gerarCodigoUnico() {
        return UUID.randomUUID().toString();
    }
}
