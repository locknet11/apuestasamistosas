
package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Equipos;
import com.apuestasamistosas.app.entities.Eventos;
import com.apuestasamistosas.app.errors.ErrorEventos;
import com.apuestasamistosas.app.repositories.EventosRepositorio;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    
    /*  Metodo de registro de evento  */
    
    @Transactional
    public void registroEvento(String nombre, Equipos equipoA, Equipos equipoB, LocalDate fechaEvento, LocalTime horaEvento) throws ErrorEventos{
        
        // validacion evento
        
        Eventos evento = new Eventos();
        LocalDateTime fechaCompleta = LocalDateTime.of(fechaEvento, horaEvento);
        evento.setNombre(nombre);
        evento.setEquipoA(equipoA);
        evento.setEquipoB(equipoB);
        evento.setExpirado(false);
        evento.setAlta(true);
        evento.setFechaEvento(fechaCompleta); 
        
        eventosRepositorio.save(evento);
    }


}
