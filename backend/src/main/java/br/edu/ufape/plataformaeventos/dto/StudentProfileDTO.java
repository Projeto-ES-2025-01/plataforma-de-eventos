package br.edu.ufape.plataformaeventos.dto;

import br.edu.ufape.plataformaeventos.util.DegreeProgram;

public class StudentProfileDTO {

    private String fullName;
    private String cpf;
    private String birthDate;
    private String phoneNumber;
    private DegreeProgram degreeProgram;
    private int currentPeriod;

    public String getFullName() {
        return fullName;
    }

    public String getCpf() {
        return cpf;
    }

    public String getBirthDate() {
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

    public void setBirthDate(String birthDate) {
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
