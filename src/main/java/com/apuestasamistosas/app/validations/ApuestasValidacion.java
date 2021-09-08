package com.apuestasamistosas.app.validations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.stereotype.Component;

import com.apuestasamistosas.app.entities.Premio;
import com.apuestasamistosas.app.entities.Eventos;
import com.apuestasamistosas.app.entities.Equipos;
import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.errors.ErrorApuesta;
import com.apuestasamistosas.app.errors.ErrorEquipos;
import com.apuestasamistosas.app.errors.ErrorEventos;
import com.apuestasamistosas.app.errors.ErrorPremio;
import com.apuestasamistosas.app.errors.ErrorUsuario;
import com.apuestasamistosas.app.repositories.EquiposRepositorio;
import com.apuestasamistosas.app.repositories.EventosRepositorio;
import com.apuestasamistosas.app.repositories.PremioRepositorio;
import com.apuestasamistosas.app.repositories.UsuarioRepositorio;

@Component
public class ApuestasValidacion {
	
	@Autowired
	private EventosRepositorio eventoRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private EquiposRepositorio equipoRepositorio;
	
	@Autowired
	private PremioRepositorio premioRepositorio;
	
	public void validarPrimerEtapa(String idReward, String idEvent, String idTeam) throws ErrorApuesta {
		
		Optional<Premio> thisPremio = premioRepositorio.findById(idReward);
		Optional<Eventos> thisEvento = eventoRepositorio.findById(idEvent);
		Optional<Equipos> thisEquipo = equipoRepositorio.findById(idTeam);
		
		if(!thisPremio.isPresent()) {
			throw new ErrorApuesta(ErrorPremio.NO_EXISTS);
		}
		
		if(!thisEvento.isPresent()) {
			throw new ErrorApuesta(ErrorEventos.NO_EXISTS);
		}
		
		if(!thisEquipo.isPresent()) {
			throw new ErrorApuesta(ErrorEquipos.NO_EXISTS);
		}
	}
	
	public void validarSegundaEtapa(String idUser, Authentication auth) throws ErrorApuesta{
		
		Optional<Usuario> thisUser = usuarioRepositorio.findById(idUser);
		
		if(thisUser.isPresent()) {
			Usuario usuario = thisUser.get();
			if(!usuario.getEmail().equals(auth.getUsername())) {
				throw new ErrorApuesta(ErrorUsuario.USER_MISMATCH);
			}
		}else {
			throw new ErrorApuesta(ErrorUsuario.NO_EXISTE);
		}
		
		
	}
	
	
}







