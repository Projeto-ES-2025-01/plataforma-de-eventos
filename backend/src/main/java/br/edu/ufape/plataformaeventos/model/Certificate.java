package br.edu.ufape.plataformaeventos.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Event event;
    
    @ManyToOne
    private StudentProfile participant;

    private LocalDate issueDate;

    private String certificateCode;

    public Certificate() {
    }

    public Certificate(Event event, StudentProfile participant, LocalDate issueDate, String certificateCode) {
        this.event = event;
        this.participant = participant;
        this.issueDate = issueDate;
        this.certificateCode = certificateCode;
    }

    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public StudentProfile getParticipant() {
        return participant;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public String getCertificateCode() {
        return certificateCode;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setParticipant(StudentProfile participant) {
        this.participant = participant;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }


}
