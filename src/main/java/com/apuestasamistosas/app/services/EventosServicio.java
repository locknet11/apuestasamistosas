
package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Equipos;
import com.apuestasamistosas.app.entities.Eventos;
import com.apuestasamistosas.app.errors.ErrorEventos;
import com.apuestasamistosas.app.repositories.EquiposRepositorio;
import com.apuestasamistosas.app.repositories.EventosRepositorio;
import com.apuestasamistosas.app.validations.EventosValidacion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventosServicio {
    
    Logger logger = LoggerFactory.getLogger(EventosServicio.class);
    
    @Autowired
    private EventosRepositorio eventosRepositorio;
    
    @Autowired
    private EventosValidacion ev;
    
    @Autowired
    private EquiposRepositorio equipoRepositorio;
    
    /*  Metodo de registro de evento  */
    
    @Transactional
    public void registroEvento(String nombre, String equipoA, String equipoB, LocalDate fechaEvento, LocalTime horaEvento) throws ErrorEventos{
        
        ev.validarDatos(equipoA, equipoB, fechaEvento, horaEvento);
        
        Optional<Equipos> thisTeamA = equipoRepositorio.findByName(equipoA);
        Optional<Equipos> thisTeamB = equipoRepositorio.findByName(equipoB);
        
        Equipos a = thisTeamA.get();
        Equipos b = thisTeamB.get();
        
        Eventos evento = new Eventos();
        LocalDateTime fechaCompleta = LocalDateTime.of(fechaEvento, horaEvento);
        evento.setNombre(nombre);
        evento.setEquipoA(a);
        evento.setEquipoB(b);
        evento.setExpirado(false);
        evento.setAlta(true);
        evento.setFechaEvento(fechaCompleta); 
        
        eventosRepositorio.save(evento);
    }
    
    
    /* Metodos de listado   */
    
    public long contarTodos(){
        return eventosRepositorio.count();
    }
    
}
