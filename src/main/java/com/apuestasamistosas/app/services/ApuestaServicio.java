package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Apuesta;

import com.apuestasamistosas.app.entities.Eventos;
import com.apuestasamistosas.app.entities.Premio;
import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.enums.EstadoApuesta;
import com.apuestasamistosas.app.errors.ErrorApuesta;
import static com.apuestasamistosas.app.errors.ErrorApuesta.NULL_apuesta;
import static com.apuestasamistosas.app.errors.ErrorApuesta.NULL_evento;
import static com.apuestasamistosas.app.errors.ErrorApuesta.NULL_premio;
import com.apuestasamistosas.app.repositories.ApuestaRepositorio;
import com.apuestasamistosas.app.repositories.EventosRepositorio;
import com.apuestasamistosas.app.repositories.PremioRepositorio;
import com.apuestasamistosas.app.repositories.UsuarioRepositorio;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApuestaServicio {

    @Autowired
    private ApuestaRepositorio apuestaRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRespositorio;
    @Autowired
    private EventosRepositorio eventosRespositorio;
    @Autowired
    private PremioRepositorio premioRespositorio;

    private ZoneId argentina = ZoneId.of("America/Argentina/Buenos_Aires");
    private final LocalDateTime hoy = LocalDateTime.now(this.argentina);

    public void crearApuesta(String idUsuario1, String idEvento, String idPremio) throws ErrorApuesta, Exception {
        Apuesta apuesta = new Apuesta();
        apuesta.setEstado(EstadoApuesta.PENDIENTE);
        apuesta.setFechaApuesta(this.hoy);
        Usuario usuarioA = usuarioRespositorio.findById(idUsuario1).get();
        apuesta.setUsuarioA(usuarioA);
        Optional<Eventos> thisEvento = eventosRespositorio.findById(idEvento);
        if (thisEvento.isPresent()) {
            Eventos evento = thisEvento.get();
            apuesta.setEvento(evento);

        } else {
            throw new ErrorApuesta(NULL_evento);

        }

        Optional<Premio> thisPremio = premioRespositorio.findById(idPremio);
        if (thisPremio.isPresent()) {
            Premio premio = thisPremio.get();
            apuesta.setPremio(premio);

        } else {
            throw new ErrorApuesta(NULL_premio);

        }
        apuestaRepositorio.save(apuesta);
    }

    public void confirmarApuesta(String idUsuario2, String idApuesta) throws ErrorApuesta, Exception {
        Usuario usuarioB = usuarioRespositorio.findById(idUsuario2).get();
        Optional<Apuesta> thisApuesta = apuestaRepositorio.findById(idApuesta);
        Integer plazoMaximo = 86400000;

        if (thisApuesta.isPresent()) {
            Apuesta apuesta = thisApuesta.get();
            if (apuesta.getEstado() == EstadoApuesta.PENDIENTE) {
                Long distanciaEntreFechas = this.hoy.until(apuesta.getFechaApuesta(), ChronoUnit.MILLIS);
                if (distanciaEntreFechas < plazoMaximo) {
                    apuesta.setUsuarioB(usuarioB);
                    apuesta.setEstado(EstadoApuesta.CONFIRMADA);
                } else {
                    apuesta.setEstado(EstadoApuesta.EXPIRADA);
                    throw new ErrorApuesta(ErrorApuesta.EXPIRADA_APUESTA);
                }

            }else if(apuesta.getEstado() == EstadoApuesta.CONFIRMADA){
                 throw new ErrorApuesta(ErrorApuesta.CONFIRMADA_APUESTA);
                
            }else if (apuesta.getEstado() == EstadoApuesta.RECHAZADA){
                throw new ErrorApuesta(ErrorApuesta.RECHAZADA_APUESTA);
            }else if (apuesta.getEstado() == EstadoApuesta.EXPIRADA){
               throw new ErrorApuesta(ErrorApuesta.EXPIRADA_APUESTA); 
            }
                
            apuestaRepositorio.save(apuesta);

        } else {
            throw new ErrorApuesta(NULL_apuesta);
        }

    }

}
