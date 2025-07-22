package br.edu.ufape.plataformaeventos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import br.edu.ufape.plataformaeventos.dto.AuthDTO;
import br.edu.ufape.plataformaeventos.dto.LoginResponseDTO;
import br.edu.ufape.plataformaeventos.dto.UserDTO;
import br.edu.ufape.plataformaeventos.model.User;
import br.edu.ufape.plataformaeventos.security.TokenService;
import br.edu.ufape.plataformaeventos.service.AuthService;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthDTO authDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(
            authDTO.getEmail(), authDTO.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword); 
        
        String token = this.tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserDTO userDTO) {
        User registeredUser = this.authService.register(userDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
}
