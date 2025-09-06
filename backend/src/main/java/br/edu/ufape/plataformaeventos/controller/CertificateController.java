package br.edu.ufape.plataformaeventos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/send")
    public Certificate sendCertificate(@RequestParam Long idEvent, @RequestParam String cpf) {
        Event evento = eventService.findById(idEvent);
        StudentProfile participante = studentProfileService.findByCPF(cpf);
        return certificateService.sendCertificate(evento, participante);
    }

    @PostMapping("/sendAll")
    public ResponseEntity<List<Certificate>> sendCertificatesForEvent(@RequestParam Long eventId) {
        List<Certificate> certificates = certificateService.sendCertificatesForEvent(eventId);
        return ResponseEntity.ok(certificates);
    }
    

}
