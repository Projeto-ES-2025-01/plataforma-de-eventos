package br.edu.ufape.plataformaeventos.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EventDTO {

    @NotBlank(message = "Nome não pode ser vazio")
    @Size(min = 3, max = 50, message = "Nome deve ter entre 3 e 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ]+$", message = "Nome deve conter apenas letras")
    private String name;

    @NotBlank(message = "Data é obrigatório")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @NotBlank(message = "Hora é obrigatório")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    @NotBlank(message = "Localização é obrigatório")
    private String location;

    @NotBlank(message = "Descrição não pode ser vazio")
    @Size(min = 50, max = 1000, message = "Nome deve ter entre 50 e 1000 caracteres")
    private String description;


     public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

     public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getDescription(){
        return description;
    } 
    
    public void setDescription(String description){
        this.description = description;
    }

}
