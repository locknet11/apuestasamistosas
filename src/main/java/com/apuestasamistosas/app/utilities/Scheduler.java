
package com.apuestasamistosas.app.utilities;

import com.apuestasamistosas.app.entities.Eventos;
import com.apuestasamistosas.app.errors.ErrorEventos;
import com.apuestasamistosas.app.errors.ErrorScheduler;
import com.apuestasamistosas.app.services.ApuestaServicio;
import com.apuestasamistosas.app.services.EventosServicio;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler{
    
    Logger logger = LoggerFactory.getLogger(Scheduler.class);
    
    @Autowired
    private EventosServicio eventoServicio;
    
    @Autowired
    private ApuestaServicio apuestaServicio;
    
    /*  En esta clase se van a definir todos los metodos relacionados con la planificacion
        de tareas. Por ejemplo, el metodo supervisorEvento estara controlando cada 1 minuto
        que la fecha de los eventos no se pasen de la fecha limite para apostar, cuando esta fecha 
        se exceda, se llamara al metodo de expirarEvento de la clase Evento.
    */
    
    @Scheduled(cron = "0 * * * * ?", zone = "America/Argentina/Buenos_Aires")
    public void supervisorEvento() throws ErrorScheduler, ErrorEventos {

        List<Eventos> thisList = eventoServicio.eventosOrdenadosPorFecha();

        if (thisList == null || thisList.isEmpty()) {
            logger.error(ErrorScheduler.NULL_EVENTS);
            throw new ErrorScheduler(ErrorScheduler.NULL_EVENTS);
        }
        
        Integer plazoMaximo = 86400000;
        ZoneId argentina = ZoneId.of("America/Argentina/Buenos_Aires");
        LocalDateTime hoy = LocalDateTime.now(argentina);

        for (Eventos evento : thisList) {
            Long distanciaEntreFechas = hoy.until(evento.getFechaEvento(), ChronoUnit.MILLIS);
            if(distanciaEntreFechas <= plazoMaximo && evento.isExpirado() == false){
                eventoServicio.expirarEvento(evento);
                logger.info("Se expiro el evento con ID \'" + evento.getId() + "\'");
            }
        }
        

    }

}
