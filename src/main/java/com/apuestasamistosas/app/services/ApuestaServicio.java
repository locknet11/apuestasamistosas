package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Apuesta;
import com.apuestasamistosas.app.entities.Equipos;
import com.apuestasamistosas.app.entities.Eventos;
import com.apuestasamistosas.app.entities.Premio;
import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.enums.EstadoApuesta;
import com.apuestasamistosas.app.enums.ResultadoApuesta;
import com.apuestasamistosas.app.errors.ErrorApuesta;
import com.apuestasamistosas.app.repositories.ApuestaRepositorio;
import com.apuestasamistosas.app.repositories.EquiposRepositorio;
import com.apuestasamistosas.app.repositories.EventosRepositorio;
import com.apuestasamistosas.app.repositories.PremioRepositorio;
import com.apuestasamistosas.app.repositories.UsuarioRepositorio;
import com.apuestasamistosas.app.validations.ApuestasValidacion;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ApuestaServicio {

    @Autowired
    private ApuestaRepositorio apuestaRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private EventosRepositorio eventosRepositorio;
    @Autowired
    private PremioRepositorio premioRepositorio;
    @Autowired
    private EquiposRepositorio equipoRepositorio;
    
    @Autowired
    private ApuestasValidacion apuestaValidacion;

    public Apuesta crearApuesta(String idUsuario1, String idEvento, String idPremio, String idEquipoUsuarioA) throws ErrorApuesta {
        ZoneId argentina = ZoneId.of("America/Argentina/Buenos_Aires");
        LocalDateTime hoy = LocalDateTime.now(argentina);
        
    	Apuesta apuesta = new Apuesta();
        apuesta.setEstado(EstadoApuesta.PENDIENTE);
        
        
        Usuario usuarioA = usuarioRepositorio.findById(idUsuario1).get();
        apuesta.setUsuarioA(usuarioA);
        
        Optional<Eventos> thisEvento = eventosRepositorio.findById(idEvento);
        Optional<Equipos> thisEquipo = equipoRepositorio.findById(idEquipoUsuarioA);
        
        if (thisEvento.isPresent()) {
        	
            Eventos evento = thisEvento.get();
            apuesta.setEvento(evento);
            apuesta.setEquipoElegidoPorUsuarioA(thisEquipo.get());
            
            if(thisEquipo.get().equals(evento.getEquipoA())) {
            	apuesta.setEquipoElegidoPorUsuarioB(evento.getEquipoB());
            }else {
            	apuesta.setEquipoElegidoPorUsuarioB(evento.getEquipoA());
            }
        } else {
            throw new ErrorApuesta(ErrorApuesta.NULL_evento);

        }

        Optional<Premio> thisPremio = premioRepositorio.findById(idPremio);
        if (thisPremio.isPresent()) {
            Premio premio = thisPremio.get();
            apuesta.setPremio(premio);

        } else {
            throw new ErrorApuesta(ErrorApuesta.NULL_premio);

        }
        apuesta.setFechaApuesta(hoy);
        apuesta.setResultadoApuesta(ResultadoApuesta.INDEFINIDO);
        Apuesta apuestaReturn = apuestaRepositorio.save(apuesta);
        return apuestaReturn;
    }

    public void confirmarApuesta(String idUsuario2, String idApuesta) throws ErrorApuesta, Exception {
        ZoneId argentina = ZoneId.of("America/Argentina/Buenos_Aires");
        LocalDateTime hoy = LocalDateTime.now(argentina);
    	Usuario usuarioB = usuarioRepositorio.findById(idUsuario2).get();
        Optional<Apuesta> thisApuesta = apuestaRepositorio.findById(idApuesta);
        Integer plazoMaximo = 86400000;

        if (thisApuesta.isPresent()) {
            Apuesta apuesta = thisApuesta.get();
            if (apuesta.getEstado() == EstadoApuesta.PENDIENTE) {
                Long distanciaEntreFechas = hoy.until(apuesta.getFechaApuesta(), ChronoUnit.MILLIS);
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
            throw new ErrorApuesta(ErrorApuesta.NULL_apuesta);
        }

    }
    
    /*	Metodos de listado */
    
    public long contarTodos() {
    	return apuestaRepositorio.count();
    }
    
    public Optional<Apuesta> buscarPorId(String id){
    	return apuestaRepositorio.findById(id);
    }
    
    /*	Aqui se llaman a los metodos de validacion  */
    
    public void llamarPrimerValidacion(String idReward, String idEvent, String idTeam) throws ErrorApuesta {
    	apuestaValidacion.validarPrimerEtapa(idReward, idEvent, idTeam);
    }
    
    public void llamarSegundaValidacion(String idUser, Authentication auth) throws ErrorApuesta{
    	apuestaValidacion.validarSegundaEtapa(idUser, auth);
    }

}
