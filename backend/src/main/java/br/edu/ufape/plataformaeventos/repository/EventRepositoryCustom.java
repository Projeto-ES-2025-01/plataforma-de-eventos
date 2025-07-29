package br.edu.ufape.plataformaeventos.repository;

import java.time.LocalDate;
import java.util.List;

import br.edu.ufape.plataformaeventos.model.Event;


public interface EventRepositoryCustom {
    
    public List<Event> findByDateBetween(LocalDate maxDate, LocalDate minDate);

}
