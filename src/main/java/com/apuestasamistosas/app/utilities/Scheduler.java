
package com.apuestasamistosas.app.utilities;

import com.apuestasamistosas.app.entities.Eventos;
import com.apuestasamistosas.app.enums.EstadoEvento;
import com.apuestasamistosas.app.errors.ErrorEventos;
import com.apuestasamistosas.app.errors.ErrorScheduler;
import com.apuestasamistosas.app.errors.ErrorTransaccion;
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
        se exceda, se llamara al metodo de expirarEvento de la clase EventoServicio.
    */
    
    @Scheduled(cron = "0 * * * * ?", zone = "America/Argentina/Buenos_Aires")
    public void supervisorEvento() throws ErrorEventos {

    	ZoneId argentina = ZoneId.of("America/Argentina/Buenos_Aires");
        LocalDateTime hoy = LocalDateTime.now(argentina);
        
        List<Eventos> thisList = eventoServicio.eventosOrdenadosPorFecha();

        if (thisList == null || thisList.isEmpty()) {
            logger.warn(ErrorScheduler.NULL_EVENTS);
        } else {

            Integer plazoMaximo = 86400000;
            
            for (Eventos evento : thisList) {
                Long distanciaEntreFechas = hoy.until(evento.getFechaEvento(), ChronoUnit.MILLIS);
                if (distanciaEntreFechas <= plazoMaximo && evento.isExpirado() == false) {
                    eventoServicio.expirarEvento(evento);
                    logger.info("Se expiro el evento con ID \'" + evento.getId() + "\'");
                }
            }

        }
    }
    
    
    /*  El metodo a continuacion cambia los estados del evento y establece un resultado aleatorio 
        El tiempo maximo usado es de 90 minutos que es la duracion estandar de un partido de futbol.
        Estos valores son relativos y esta implementacion es provisoria.
        Tambien llama al metodo informarResultados de ApuestaServicio para setear los resultados de
        las apuestas.
    */
    
    @Scheduled(cron = "0 * * * * ?", zone = "America/Argentina/Buenos_Aires")
    public void supervisorEstados() throws ErrorEventos, ErrorTransaccion {
    	ZoneId argentina = ZoneId.of("America/Argentina/Buenos_Aires");
        LocalDateTime hoy = LocalDateTime.now(argentina);

        List<Eventos> thisList = eventoServicio.eventosOrdenadosPorFecha();

        if (thisList == null || thisList.isEmpty()) {
            logger.warn(ErrorScheduler.NULL_EVENTS);
        } else {
            for (Eventos evento : thisList) {
                Long distanciaHoraria = hoy.until(evento.getFechaEvento(), ChronoUnit.MILLIS);
                
                if(evento.getEstado() == EstadoEvento.CONFIRMADO && distanciaHoraria <= 0){
                    eventoServicio.actualizarEstado(evento, EstadoEvento.EN_CURSO);
                    logger.info("Se actualizo a EN_CURSO el evento con ID \'" + evento.getId() + "\'");
                    
                }else if (distanciaHoraria <= -5400000 && evento.getEstado() == EstadoEvento.EN_CURSO){
                    
                    eventoServicio.actualizarEstado(evento, EstadoEvento.FINALIZADO);
                    logger.info("Se actualizo a FINALIZADO el evento con ID \'" + evento.getId() + "\'");
                    
                    apuestaServicio.informarResultados(eventoServicio.establecerResultado(evento));
                    logger.info("Resultado del evento: " + evento.getResultado());
                }
            }
        }
    }
    
    

}
