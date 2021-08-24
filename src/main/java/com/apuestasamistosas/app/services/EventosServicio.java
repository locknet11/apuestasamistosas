
package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Equipos;
import com.apuestasamistosas.app.entities.Eventos;
import com.apuestasamistosas.app.enums.EstadoEvento;
import com.apuestasamistosas.app.errors.ErrorPremio;
import com.apuestasamistosas.app.repositories.EventosRepositorio;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventosServicio {
    
    @Autowired
    private EventosRepositorio eventosRepositorio;
    
     @Transactional
    public void crearEvento(String nombre,Equipos equipoA,Equipos equipoB, LocalDateTime fechaevento, EstadoEvento estado ) throws ErrorPremio{
        
     
        
        
        Eventos evento = new Eventos();
        evento.setNombre(nombre);
        evento.setEquipoA(equipoA);
        evento.setEquipoB(equipoB);
        evento.setExpirado(false);
        
        evento.setFechaEvento(fechaevento);
        evento.setEstado(estado);
        
        
        eventosRepositorio.save(evento);
    }

}
