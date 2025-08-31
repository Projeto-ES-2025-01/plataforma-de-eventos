package br.edu.ufape.plataformaeventos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ufape.plataformaeventos.model.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByParticipantId(Long participantId);
    List<Certificate> findByEventId(Long eventId);
    Certificate findByCertificateCode(String certificateCode);
}
