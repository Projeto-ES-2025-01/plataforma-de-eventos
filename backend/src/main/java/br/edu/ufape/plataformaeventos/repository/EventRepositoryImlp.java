package br.edu.ufape.plataformaeventos.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.ufape.plataformaeventos.model.Event;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class EventRepositoryImlp implements EventRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<Event> findByDateBetween(LocalDate minDate, LocalDate maxDate){
        String jpql = "SELECT l FROM Event l WHERE l.date BETWEEN :min AND :max";
        TypedQuery<Event> query = entityManager.createQuery(jpql, Event.class);
        query.setParameter("min", minDate);
        query.setParameter("max", maxDate);
        
        return query.getResultList();
    }

}