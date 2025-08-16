package br.edu.ufape.plataformaeventos.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import br.edu.ufape.plataformaeventos.dto.EventDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @SequenceGenerator(name = "event_id_seq", sequenceName = "event_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "event_id_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String location;

    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private OrganizerProfile organizer;

    @ManyToMany
    @JoinTable(
        name = "event_participants",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<StudentProfile> participants = new HashSet<>();

    public Event() {
    }

    public Event(String name, LocalDate date, LocalTime time, String location, String description, OrganizerProfile organizer) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.description = description;
        this.organizer = organizer;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public OrganizerProfile getOrganizer() {
        return organizer;
    }

    public Set<StudentProfile> getParticipants() {
        return participants;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOrganizer(OrganizerProfile organizer) {
        this.organizer = organizer;
    }

    public long getIdOrganizer(){
        return organizer.getId();
    }

    public void setParticipants(Set<StudentProfile> participants) {
        this.participants = participants;
    }


    public void addParticipant(StudentProfile participant) {
        this.participants.add(participant);
    }


    public EventDTO eventToEventDTO() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setName(this.name);
        eventDTO.setDate(this.date);
        eventDTO.setTime(this.time);
        eventDTO.setLocation(this.location);
        eventDTO.setDescription(this.description);
        eventDTO.setIdOrganizer(this.organizer.getId());
        return eventDTO;
    }

}
