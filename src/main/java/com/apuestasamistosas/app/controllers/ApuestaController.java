
package com.apuestasamistosas.app.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.apuestasamistosas.app.entities.Apuesta;
import com.apuestasamistosas.app.entities.Equipos;
import com.apuestasamistosas.app.entities.Eventos;
import com.apuestasamistosas.app.entities.Premio;
import com.apuestasamistosas.app.errors.ErrorApuesta;
import com.apuestasamistosas.app.errors.ErrorPremio;
import com.apuestasamistosas.app.services.ApuestaServicio;
import com.apuestasamistosas.app.services.EquiposServicio;
import com.apuestasamistosas.app.services.EventosServicio;
import com.apuestasamistosas.app.services.PremioServicio;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/bets")
@PreAuthorize("hasAnyRole('ROLE_USUARIO')")
@Slf4j
public class ApuestaController {
	
	@Autowired
	private ApuestaServicio apuestaServicio;
	
	@Autowired
	private PremioServicio premioServicio;
	
	@Autowired
	private EventosServicio eventoServicio;
	
	@Autowired 
	private EquiposServicio equipoServicio;
    
    @GetMapping
    public String index(){
        return "apuestas/index";
    }
    
    /* Crear apuesta desde la vista premios */
    
	@GetMapping("/fromRewards/{id}")
	public String betFromRewards(@PathVariable String id, ModelMap model) throws ErrorApuesta {
		Optional<Premio> thisPremio = premioServicio.buscarPorId(id);

		if (thisPremio.isPresent()) {
			Premio premio = thisPremio.get();
			model.addAttribute("source", "rewards");
			model.addAttribute("premio", premio);
			model.addAttribute("listaEventos", eventoServicio.eventosOrdenadosPorFechaYSinExpirar());
			return "apuestas/crear-apuesta";
		} else {
			return "redirect:/rewards";
		}

	}
	
	/*	Crear apuesta desde la vista eventos */
	
	@GetMapping("/fromEvents")
	public String betFromEvents(
			@RequestParam(name = "idEvent", required = false) String idEvent,
			@RequestParam(name = "idTeam", required = false) String idTeam,
			ModelMap model) throws ErrorApuesta, ResponseStatusException{
		
		Optional<Eventos> thisEvento = eventoServicio.buscarPorId(idEvent);
		Optional<Equipos> thisEquipo = equipoServicio.buscarPorId(idTeam);
		
		if(idEvent == null || idTeam == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		if(thisEvento.isPresent() && thisEquipo.isPresent()) {
			Equipos equipo = thisEquipo.get();
			Eventos evento = thisEvento.get();
			model.addAttribute("evento", evento);
			model.addAttribute("equipo", equipo);
			model.addAttribute("source", "events");
			model.addAttribute("premios", premioServicio.listarTodos());
			return "apuestas/crear-apuesta";
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
	}
	
	
	/* Paso intermedio que define la condiciones de la apuesta */
	
	@GetMapping("/beginBet")
	public String createBet(
			@RequestParam(name = "idReward", required = false) String idReward,
			@RequestParam(name = "idEvent", required = false) String idEvent,
			@RequestParam(name = "idTeam", required = false) String idTeam, ModelMap model) throws ErrorApuesta{
		
		Optional<Premio> thisPremio = premioServicio.buscarPorId(idReward);
		Optional<Eventos> thisEvento = eventoServicio.buscarPorId(idEvent);
		Optional<Equipos> thisEquipo = equipoServicio.buscarPorId(idTeam);
		
		if(thisPremio.isPresent() && thisEvento.isPresent() && thisEquipo.isPresent()) {
			try {
				apuestaServicio.llamarPrimerValidacion(idReward, idEvent, idTeam);
				model.addAttribute("premio", thisPremio.get());
				model.addAttribute("evento", thisEvento.get());
				model.addAttribute("equipo", thisEquipo.get());
				return "apuestas/checkout-apuesta";
			}catch(ErrorApuesta e) {
				return "redirect:/error";
			}
		}else {
			return "redirect:/error";
		}
	}
	
	/* Paso final el proceso de creacion de la apuesta */
	
	@PostMapping("/payBet")
	public String checkoutBet(
			@RequestParam(name = "idReward", required = false) String idReward,
			@RequestParam(name = "idEvent", required = false) String idEvent,
			@RequestParam(name = "idTeam", required = false) String idTeam,
			@RequestParam(name = "idUser", required = false) String idUser,
			ModelMap model, RedirectAttributes redirectAttrs) throws ErrorApuesta {
		
		 try {
			 Apuesta apuesta = apuestaServicio.crearApuesta(idUser, idEvent, idReward, idTeam);
			 redirectAttrs.addAttribute("id", apuesta.getId());
			 return "redirect:/bets/postCheckout/{id}";
		 }catch(ErrorApuesta e) {
			 return "redirect:/bets/postCheckout/error";
		 }
	}
	
	/*	En este punto se devuelve una vista dandole al usuario un link para compartir en
	 * 	caso de que la apuesta se haya creado exitosamente, caso contrario, devuelve vista de error
	 *  */
	
	@GetMapping(value = {"/postCheckout/error", "/postCheckout/{id}"})
	public String postCheckout(@PathVariable(name = "id", required = false) String id, ModelMap model) throws ResponseStatusException{
		
			if(id != null) {
				Optional<Apuesta> thisApuesta = apuestaServicio.buscarPorId(id);
				if(thisApuesta.isPresent()) {
					model.addAttribute("message", "¡Apuesta creada exitosamente!");
					model.addAttribute("idBet", id);
					return "apuestas/post-checkout";
				}else {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND);
				}
			}else {
				model.addAttribute("error", "Se ha producido un error al crear la apuesta");
				return "apuestas/post-checkout";
			}
	}
	
	
	
  

}
