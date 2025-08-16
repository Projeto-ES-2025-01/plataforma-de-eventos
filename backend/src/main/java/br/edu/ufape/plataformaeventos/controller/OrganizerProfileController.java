package br.edu.ufape.plataformaeventos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.plataformaeventos.service.OrganizerProfileService;


@RestController
@RequestMapping("/organizer")
public class OrganizerProfileController {

    @Autowired
    OrganizerProfileService organizerProfileService;

    @GetMapping("/getId/{email}")
    public String getMethodName(@PathVariable String email) {
        return organizerProfileService.getOrganizerId(email);
    }
    
}
