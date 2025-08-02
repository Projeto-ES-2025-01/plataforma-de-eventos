package br.edu.ufape.plataformaeventos.dto;

import java.time.LocalDate;

import br.edu.ufape.plataformaeventos.util.DegreeProgram;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StudentProfileDTO {

    @NotBlank(message = "Nome completo é obrigatório")
    private String fullName;
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;
    @NotNull(message = "Data de nascimento é obrigatória")
    private LocalDate birthDate;
    @NotBlank(message = "Número de telefone é obrigatório")
    private String phoneNumber;
    @NotNull(message = "Curso é obrigatório")
    private DegreeProgram degreeProgram;
    @NotNull(message = "Período atual é obrigatório")
    private int currentPeriod;

    public String getFullName() {
        return fullName;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public DegreeProgram getDegreeProgram() {
        return degreeProgram;
    }

    public int getCurrentPeriod() {
        return currentPeriod;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDegreeProgram(DegreeProgram degreeProgram) {
        this.degreeProgram = degreeProgram;
    }

    public void setCurrentPeriod(int currentPeriod) {
        this.currentPeriod = currentPeriod;
    }



}
