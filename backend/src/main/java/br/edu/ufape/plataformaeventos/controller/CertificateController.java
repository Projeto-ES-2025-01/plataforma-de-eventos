package br.edu.ufape.plataformaeventos.controller;

import java.util.List;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.plataformaeventos.model.Certificate;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.service.CertificateService;
import br.edu.ufape.plataformaeventos.service.EventService;
import br.edu.ufape.plataformaeventos.service.StudentProfileService;



@RestController
@RequestMapping("/certificate")
public class CertificateController {

    private CertificateService certificateService;
    private EventService eventService;
    private StudentProfileService studentProfileService;

    public CertificateController(CertificateService certificateService, EventService eventService, StudentProfileService studentProfileService) {
        this.certificateService = certificateService;
        this.eventService = eventService;
        this.studentProfileService = studentProfileService;
    }

      @PostMapping("/send/{idEvent}/{email}")
    public Certificate sendCertificate(@PathVariable Long idEvent, @PathVariable String email) {
        Event evento = eventService.findById(idEvent);
        StudentProfile participante = studentProfileService.findByEmail(email);
        return certificateService.sendCertificate(evento, participante);
    }

    @PostMapping("/sendAll/{eventId}")
    public ResponseEntity<List<Certificate>> sendCertificatesForEvent(@PathVariable Long eventId) {
        List<Certificate> certificates = certificateService.sendCertificatesForEvent(eventId);
        return ResponseEntity.ok(certificates);
    }

    @GetMapping("/getByParticipantEventId/{eventId}/{studentEmail}")
    public Long getByParticipantAndEventId(@PathVariable Long eventId, @PathVariable String studentEmail) {
        Event event = eventService.findById(eventId);
        StudentProfile participant = studentProfileService.findByEmail(studentEmail);
        return certificateService.getIdCertificate(event, participant);
    }
    

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getCertificatePdf(@PathVariable Long id) {
        Certificate certificate = certificateService.findById(id);
        byte[] pdf = certificateService.generateCertificatePDF(certificate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("certificado.pdf").build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping("/verify/{code}")
    public ResponseEntity<Certificate> verifyCertificate(@PathVariable String code) {
        Certificate certificate = certificateService.findByCertificateCode(code);
        if (certificate != null) {
            return ResponseEntity.ok(certificate);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}