package com.apuestasamistosas.app.validations;

import com.apuestasamistosas.app.entities.Equipos;
import com.apuestasamistosas.app.errors.ErrorEventos;
import com.apuestasamistosas.app.repositories.EquiposRepositorio;
import com.apuestasamistosas.app.repositories.EventosRepositorio;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventosValidacion {

    Logger logger = LoggerFactory.getLogger(EventosValidacion.class);

    @Autowired
    private EventosRepositorio eventoRepositorio;

    @Autowired
    private EquiposRepositorio equipoRepositorio;

    public void validarDatos(String equipoA, String equipoB, LocalDate fechaEvento, LocalTime horaEvento) throws ErrorEventos {

        if (equipoA == null || equipoA.isEmpty() || equipoB == null || equipoB.isEmpty()) {
            logger.error(ErrorEventos.NULL_TEAM);
            throw new ErrorEventos(ErrorEventos.NULL_TEAM);
        }

        if (equipoA.equals(equipoB)) {
            logger.error(ErrorEventos.SAME_TEAM);
            throw new ErrorEventos(ErrorEventos.SAME_TEAM);
        }

        Optional<Equipos> thisEquipoA = equipoRepositorio.findByName(equipoA);
        Optional<Equipos> thisEquipoB = equipoRepositorio.findByName(equipoB);

        if (!thisEquipoA.isPresent() || !thisEquipoB.isPresent()) {
            logger.error(ErrorEventos.NO_TEAM);
            throw new ErrorEventos(ErrorEventos.NO_TEAM);
        }

        if (fechaEvento == null) {
            logger.error(ErrorEventos.NULL_DATE);
            throw new ErrorEventos(ErrorEventos.NULL_DATE);
        }

        if (horaEvento == null) {
            logger.error(ErrorEventos.NULL_TIME);
            throw new ErrorEventos(ErrorEventos.NULL_TIME);
        }

        LocalDateTime hoy = LocalDateTime.of(LocalDate.now(), LocalTime.now());

        LocalDateTime fechaCompleta = LocalDateTime.of(fechaEvento, horaEvento);

        Long distanciaEntreFechas = hoy.until(fechaCompleta, ChronoUnit.MILLIS);

        Integer plazo = 172800000;

        if (distanciaEntreFechas < plazo) {
            logger.error(ErrorEventos.WRONG_DATE);
            throw new ErrorEventos(ErrorEventos.WRONG_DATE);
        }

    }

}
