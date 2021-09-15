package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Apuesta;
import com.apuestasamistosas.app.entities.Equipos;
import com.apuestasamistosas.app.entities.Eventos;
import com.apuestasamistosas.app.entities.Premio;
import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.enums.EstadoApuesta;
import com.apuestasamistosas.app.enums.ResultadoApuesta;
import com.apuestasamistosas.app.enums.ResultadoEvento;
import com.apuestasamistosas.app.errors.ErrorApuesta;
import com.apuestasamistosas.app.errors.ErrorTransaccion;
import com.apuestasamistosas.app.repositories.ApuestaRepositorio;
import com.apuestasamistosas.app.repositories.EquiposRepositorio;
import com.apuestasamistosas.app.repositories.EventosRepositorio;
import com.apuestasamistosas.app.repositories.PremioRepositorio;
import com.apuestasamistosas.app.repositories.UsuarioRepositorio;
import com.apuestasamistosas.app.validations.ApuestasValidacion;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
    private MailServicio mailServicio;
    
    @Autowired
    private ApuestasValidacion apuestaValidacion;

    public Apuesta crearApuesta(String idUsuario1, String idEvento, String idPremio, String idEquipoUsuarioA, Boolean paymentMethod) throws ErrorApuesta, ErrorTransaccion {
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
            premio.setRanking(premio.getRanking() + 1);
        } else {
            throw new ErrorApuesta(ErrorApuesta.NULL_premio);

        }
        apuesta.setFechaApuesta(hoy);
        apuesta.setResultadoApuesta(ResultadoApuesta.INDEFINIDO);
        Apuesta apuestaReturn = apuestaRepositorio.saveAndFlush(apuesta);
        return apuestaReturn;
    }

    @Transactional
    public void confirmarApuesta(String idUsuario2, String idApuesta, Boolean paymentMethod) throws ErrorApuesta, ErrorTransaccion {
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
                
            Apuesta mailApuesta = apuestaRepositorio.save(apuesta);
            mailServicio.betConfirmationToUserA(mailApuesta);
            mailServicio.betConfirmationToUserB(mailApuesta);

        } else {
            throw new ErrorApuesta(ErrorApuesta.NULL_apuesta);
        }

    }
    
    @Transactional
    public void rechazarApuesta(String idUsuario2, String idApuesta) throws ErrorApuesta, ErrorTransaccion {
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
                    apuesta.setEstado(EstadoApuesta.RECHAZADA);
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
                
            Apuesta apuestaReturn = apuestaRepositorio.save(apuesta);
            mailServicio.betRejectToUserA(apuestaReturn);

        } else {
            throw new ErrorApuesta(ErrorApuesta.NULL_apuesta);
        }

    }
    
    @Transactional
    public void informarResultados(Eventos evento) throws ErrorTransaccion {
    	
    	List<Apuesta> thisList = buscarPorEventoFinalizado(evento.getId());
    	
    	if(!thisList.isEmpty()) {
    		
    		for (Apuesta apuesta : thisList) {
    			
    			Usuario usuarioA = apuesta.getUsuarioA();
    			Usuario usuarioB = apuesta.getUsuarioB();
    			
				if(evento.getResultado() == ResultadoEvento.EMPATE) {
					apuesta.setResultadoApuesta(ResultadoApuesta.EMPATE);
					usuarioA.setEmpatados(usuarioA.getEmpatados() + 1);
					usuarioB.setEmpatados(usuarioB.getEmpatados() + 1);
				}else if(evento.getResultado() == ResultadoEvento.EQUIPO_A) {
					
					if(apuesta.getEquipoElegidoPorUsuarioA().equals(evento.getEquipoA())) {
						apuesta.setResultadoApuesta(ResultadoApuesta.USUARIO_A);
						usuarioA.setGanados(usuarioA.getGanados() + 1);
						usuarioB.setPerdidos(usuarioB.getPerdidos() + 1);
					}else {
						apuesta.setResultadoApuesta(ResultadoApuesta.USUARIO_B);
						usuarioB.setGanados(usuarioB.getGanados() + 1);
						usuarioA.setPerdidos(usuarioA.getPerdidos() + 1);
					}
				}else if(evento.getResultado() == ResultadoEvento.EQUIPO_B) {
					
					if(apuesta.getEquipoElegidoPorUsuarioA().equals(evento.getEquipoB())) {
						apuesta.setResultadoApuesta(ResultadoApuesta.USUARIO_A);
						usuarioA.setGanados(usuarioA.getGanados() + 1);
						usuarioB.setPerdidos(usuarioB.getPerdidos() + 1);
					}else {
						apuesta.setResultadoApuesta(ResultadoApuesta.USUARIO_B);
						usuarioB.setGanados(usuarioB.getGanados() + 1);
						usuarioA.setPerdidos(usuarioA.getPerdidos() + 1);
					}
				}
			
				usuarioRepositorio.save(usuarioA);
				usuarioRepositorio.save(usuarioB);
				apuesta.setEstado(EstadoApuesta.CONCLUIDA);
				Apuesta apuestaReturn = apuestaRepositorio.save(apuesta);
				mailServicio.betResultToUserA(apuestaReturn);
				mailServicio.betResultToUserB(apuestaReturn);
				
			}
    	}
    }
    
    /*	Metodos de listado */
    
    public List<Apuesta> buscarPorEventoFinalizado(String id){
    	Optional<List<Apuesta>> thisList = apuestaRepositorio.findByEvent(id);
    	if(thisList.isPresent()) {
    		return thisList.get();
    	}else {
    		return Collections.emptyList();
    	}
    }
    
    public long contarTodos() {
    	return apuestaRepositorio.count();
    }
    
    public Optional<Apuesta> buscarPorId(String id){
    	return apuestaRepositorio.findById(id);
    }
    
    public List<Apuesta> listarApuestasConfirmadas(String userId){
    	Optional<List<Apuesta>> thisList = apuestaRepositorio.findByUserAndState(userId, "CONFIRMADA");

    	if(thisList.isPresent()) {
    		return thisList.get();
    	}else {
    		return Collections.emptyList();
    	}
    }
    
    public List<Apuesta> listarApuestasPendientes(String userId){
    	Optional<List<Apuesta>> thisList = apuestaRepositorio.findByUserAndState(userId, "PENDIENTE");

    	if(thisList.isPresent()) {
    		return thisList.get();
    	}else {
    		return Collections.emptyList();
    	}
    }
    
    
    /*	Aqui se llaman a los metodos de validacion  */
    
    public void llamarPrimerValidacion(String idReward, String idEvent, String idTeam) throws ErrorApuesta {
    	apuestaValidacion.validarPrimerEtapa(idReward, idEvent, idTeam);
    }
    
    public void llamarSegundaValidacion(String idUser, Authentication auth) throws ErrorApuesta{
    	apuestaValidacion.validarSegundaEtapa(idUser, auth);
    }

}
