package br.edu.ufape.plataformaeventos.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.edu.ufape.plataformaeventos.model.Certificate;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.repository.CertificateRepository;

@Service
public class CertificateService {

    private CertificateRepository certificateRepository;

    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public Certificate sendCertificate(Event event, StudentProfile participant) {
        String code = gerarCodigoUnico();
        Certificate certificate = new Certificate(event, participant, LocalDate.now(), code);
        return certificateRepository.save(certificate);
    }

    private String gerarCodigoUnico() {
        return UUID.randomUUID().toString();
    }
}
