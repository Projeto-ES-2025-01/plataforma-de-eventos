package br.edu.ufape.plataformaeventos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.model.StudentProfile;
import br.edu.ufape.plataformaeventos.service.StudentProfileService;
import jakarta.validation.Valid;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/student") 
public class StudentProfileController {
    
    @Autowired
    StudentProfileService studentProfileService;



    @GetMapping("/getProfile/{email}")
    public ResponseEntity<StudentProfileDTO> getStudentProfile(@PathVariable String email){
        StudentProfileDTO studentAtivo = studentProfileService.getStudentProfile(email);
        if(studentAtivo != null){ 
        return ResponseEntity.status(HttpStatus.OK).body(studentAtivo);
}
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        
    }

    @PostMapping("/editProfile")
    public ResponseEntity<Void> editStudentProfile(@RequestBody @Valid StudentProfileDTO studentProfileDTO){
        if (studentProfileDTO.getCpf() == null || studentProfileDTO.getCpf().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else {
            studentProfileService.updateStudentProfile(studentProfileDTO);
            return ResponseEntity.status(HttpStatus.OK).build();}
    }
}
