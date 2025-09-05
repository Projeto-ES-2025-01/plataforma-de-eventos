package br.edu.ufape.plataformaeventos.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.plataformaeventos.service.OrganizerProfileService;


@RestController
@RequestMapping("/organizer")
public class OrganizerProfileController {

    private final OrganizerProfileService organizerProfileService;

    public OrganizerProfileController (OrganizerProfileService organizerProfileService){
        this.organizerProfileService = organizerProfileService;

    }

    @GetMapping("/getId/{email}")
    public String getMethodName(@PathVariable String email) {
        return organizerProfileService.getOrganizerId(email);
    }
    
}
