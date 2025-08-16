package br.edu.ufape.plataformaeventos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "organizer_profiles")
public class OrganizerProfile {

    @Id
    @SequenceGenerator(name = "organizer_profile_id_seq", sequenceName = "organizer_profile_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "organizer_profile_id_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;


    public OrganizerProfile() {

    }

    public OrganizerProfile(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

  

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }



}
