package br.edu.ufape.plataformaeventos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.model.Event;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.service.EventService;
import br.edu.ufape.plataformaeventos.service.StudentProfileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/student") 
public class StudentProfileController {
    
    private final StudentProfileService studentProfileService;
    private final EventService eventService;

    public StudentProfileController(StudentProfileService studentProfileService,EventService eventService){
        this.eventService = eventService;
        this.studentProfileService = studentProfileService;
    }

    @GetMapping("/getProfile/{email}")
    public ResponseEntity<StudentProfileDTO> getStudentProfile(@PathVariable String email){
        StudentProfileDTO studentAtivo = studentProfileService.getStudentProfile(email);
        if(studentAtivo != null){ 
        return ResponseEntity.status(HttpStatus.OK).body(studentAtivo);
}
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        
    }

    @PutMapping("/editProfile")
    public ResponseEntity<String> editStudentProfile(@RequestBody @Valid StudentProfileDTO studentProfileDTO){
        if (studentProfileDTO.getCpf() == null || studentProfileDTO.getCpf().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else {
            studentProfileService.updateStudentProfile(studentProfileDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Usuario editado com sucesso.");}
    }

    @DeleteMapping("/deleteProfile/{email}")
    public ResponseEntity<Void> deleteStudentProfile(@PathVariable String email) {
        try {
            studentProfileService.deleteStudentProfile(email);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    @PostMapping("/joinEvent/{eventId}")
    public ResponseEntity<String> joinEvent(@PathVariable long eventId, @RequestBody StudentProfileDTO studentProfileDTO){
        Event event = eventService.findById(eventId);
        if(event==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento não encontrado");
        }
        StudentProfile participant = studentProfileService.findByCPF(studentProfileDTO.getCpf());
        if(participant==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estudante não encontrado");
        }
        eventService.addParticipantToEvent(event,participant);
        return ResponseEntity.status(HttpStatus.OK).body("Participante adicionado ao evento com sucesso");
        
    }

    @Transactional
    @DeleteMapping("/leaveEvent/{eventId}")
    public ResponseEntity<String> leaveEvent(@PathVariable long eventId, @RequestBody StudentProfileDTO studentProfileDTO){
        Event event = eventService.findById(eventId);
        if(event==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento não encontrado");
        }
        StudentProfile participant = studentProfileService.findByCPF(studentProfileDTO.getCpf());
        if(participant==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estudante não encontrado");
        }
        eventService.removeParticipantFromEvent(eventId,participant.getId());
        return ResponseEntity.status(HttpStatus.OK).body("Participante removido do evento com sucesso");
        
    }

    
    @GetMapping("/allProfiles")
    public ResponseEntity<List<StudentProfileDTO>> getAllStudentProfiles() {
        List<StudentProfileDTO> studentProfiles = studentProfileService.getAllStudentProfiles();
        if (studentProfiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(studentProfiles);
    }
}
