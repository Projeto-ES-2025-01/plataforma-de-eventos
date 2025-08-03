package br.edu.ufape.plataformaeventos.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.edu.ufape.plataformaeventos.dto.StudentProfileDTO;
import br.edu.ufape.plataformaeventos.util.DegreeProgram;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    @SequenceGenerator(name = "student_profile_id_seq", sequenceName = "student_profile_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "student_profile_id_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DegreeProgram degreeProgram;

    @Column(nullable = false)
    private int currentPeriod;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToMany(mappedBy = "participants")
    @JsonBackReference
    private Set<Event> events = new HashSet<>();;

    public StudentProfile() {
        
    }

    public StudentProfile(String fullName, String cpf, LocalDate birthDate, String phone, DegreeProgram degreeProgram, Integer currentPeriod, User user) {
        this.fullName = fullName;
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.phoneNumber = phone;
        this.degreeProgram = degreeProgram;
        this.currentPeriod = currentPeriod;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

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

    public User getUser() {
        return user;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        this.events.add(event);
        
    }
    

    public StudentProfileDTO toDTO() {
    StudentProfileDTO dto = new StudentProfileDTO();
    dto.setFullName(this.getFullName());
    dto.setCpf(this.getCpf());
    dto.setBirthDate(this.getBirthDate());
    dto.setPhoneNumber(this.getPhoneNumber());
    dto.setDegreeProgram(this.getDegreeProgram());
    dto.setCurrentPeriod(this.getCurrentPeriod());
    return dto;
}

}
