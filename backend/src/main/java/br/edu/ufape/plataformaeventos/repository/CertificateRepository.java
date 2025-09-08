package br.edu.ufape.plataformaeventos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.plataformaeventos.model.Certificate;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.StudentProfile;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByParticipant(StudentProfile participant);
    List<Certificate> findByEventId(Event event);
    Certificate findByParticipantAndEventId(Event event, StudentProfile participant);
    Certificate findByCertificateCode(String certificateCode);
    Certificate findByEventAndParticipant(Event event, StudentProfile participant);
}
