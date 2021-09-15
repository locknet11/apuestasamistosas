
package com.apuestasamistosas.app.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.apuestasamistosas.app.enums.EstadoApuesta;
import com.apuestasamistosas.app.errors.ErrorApuesta;
import com.apuestasamistosas.app.errors.ErrorTransaccion;
import com.apuestasamistosas.app.services.ApuestaServicio;
import com.apuestasamistosas.app.services.EquiposServicio;
import com.apuestasamistosas.app.services.EventosServicio;
import com.apuestasamistosas.app.services.PremioServicio;
import com.apuestasamistosas.app.services.TransaccionServicio;

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
				model.addAttribute("source", "beginBet");
				return "apuestas/checkout-apuesta";
			}catch(ErrorApuesta e) {
				return "redirect:/error";
			}
		}else {
			return "redirect:/error";
		}
	}
	
	/* Paso final el proceso de creacion de la apuesta */
	
	@GetMapping("/payBet")
	public String checkoutBet(@RequestParam(name = "idReward", required = false) String idReward,
			@RequestParam(name = "idEvent", required = false) String idEvent,
			@RequestParam(name = "idTeam", required = false) String idTeam,
			@RequestParam(name = "idUser", required = false) String idUser,
			@RequestParam(name = "paymentMethod", required = false) String payment, ModelMap model,
			RedirectAttributes redirectAttrs) throws ErrorApuesta {

		Optional<Premio> thisPremio = premioServicio.buscarPorId(idReward);
		Optional<Eventos> thisEvento = eventoServicio.buscarPorId(idEvent);
		Optional<Equipos> thisEquipo = equipoServicio.buscarPorId(idTeam);
		try {

			Boolean paymentMethod;

			if (payment.equals("betbuy")) {
				paymentMethod = false;
			} else if (payment.equals("mercadopago")) {
				paymentMethod = true;
			} else {
				paymentMethod = true;
			}

			Apuesta apuesta = apuestaServicio.crearApuesta(idUser, idEvent, idReward, idTeam, paymentMethod);
			redirectAttrs.addAttribute("id", apuesta.getId());
			return "redirect:/bets/postCheckout/{id}";
		} catch (ErrorApuesta e) {
			model.addAttribute("premio", thisPremio.get());
			model.addAttribute("evento", thisEvento.get());
			model.addAttribute("equipo", thisEquipo.get());
			model.put("error", e.getMessage());
			model.addAttribute("source", "beginBet");
			return "apuestas/checkout-apuesta";
		} catch (ErrorTransaccion ex) {
			model.addAttribute("premio", thisPremio.get());
			model.addAttribute("evento", thisEvento.get());
			model.addAttribute("equipo", thisEquipo.get());
			model.put("error", ex.getMessage());
			model.addAttribute("source", "beginBet");
			return "apuestas/checkout-apuesta";
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
	
	/* Metodo que muestra la vista de rechazar o confirmar apuesta  */
	
	@GetMapping(value = {"/join/{id}", "/join"})
	public String joinBet(@PathVariable(required = false) String id, ModelMap model) throws ResponseStatusException{
		
		if(id == null || id.isEmpty()) {
			return "redirect:/";
		}
		
		Optional<Apuesta> thisApuesta = apuestaServicio.buscarPorId(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		
		if(thisApuesta.isPresent()) {
			
			Apuesta apuesta = thisApuesta.get();
			
			if (auth.getName().equals(apuesta.getUsuarioA().getEmail())) {
				return "redirect:/bets/summary/" + apuesta.getId();
			}
			
			if(apuesta.getEstado() == EstadoApuesta.PENDIENTE) {
				model.addAttribute("estadoApuesta", "pendiente");
				model.addAttribute("apuesta", apuesta);
			}
			
			if(apuesta.getEstado() == EstadoApuesta.EXPIRADA) {
				model.addAttribute("estadoApuesta", "expirada");
			}
			
			if(apuesta.getEstado() == EstadoApuesta.RECHAZADA) {
				model.addAttribute("estadoApuesta", "rechazada");
			}
			
			if(apuesta.getEstado() == EstadoApuesta.CONFIRMADA) {
				model.addAttribute("estadoApuesta", "confirmada");
			}
			
			return "apuestas/opcion-apuesta";
			
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
	}
	
	/*	En este metodo se devuelve al usuario que acepto la invitacion una vista para
	 *  que pague la otra parte de la apuesta, una vez pagada se devuelve una vista de apuesta concretada
	 *  */
	
	@GetMapping(value = { "/confirmCheckout/{id}", "/confirmCheckout" })
	public String betConfirm(@PathVariable(name = "id") String idBet,
			ModelMap model) throws ErrorApuesta{

		if (idBet == null || idBet.isEmpty()) {
			return "redirect:/";
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<Apuesta> thisApuesta = apuestaServicio.buscarPorId(idBet);

		if (thisApuesta.isPresent()) {
			Apuesta apuesta = thisApuesta.get();

			if (auth.getName().equals(apuesta.getUsuarioA().getEmail())) {
				return "redirect:/bets/summary/" + apuesta.getId();
			}
			
			model.addAttribute("source", "confirmCheckout");
			model.addAttribute("apuesta", apuesta);
			return "apuestas/checkout-apuesta";

		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	/* Metodo de confirmacion de la apuesta */
	
	@GetMapping(value = {"/confirm"})
	public String confirm(@RequestParam(name = "idBet", required = false) String idBet,
			@RequestParam(name = "idUserB", required = false) String idUserB,
			@RequestParam(name = "paymentMethod", required = false) String payment,
			ModelMap model) throws ErrorApuesta, ResponseStatusException {
		
		Optional<Apuesta> thisApuesta = apuestaServicio.buscarPorId(idBet);
		
		
		Boolean paymentMethod;

		if (payment.equals("betbuy")) {
			paymentMethod = false;
		} else if (payment.equals("mercadopago")) {
			paymentMethod = true;
		} else {
			paymentMethod = true;
		}
		
		if(idBet == null || idBet.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		if(idUserB == null || idUserB.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		try {
			apuestaServicio.confirmarApuesta(idUserB, idBet, paymentMethod);
			model.addAttribute("estado", "success");
			return "apuestas/apuesta-post";
		}catch(ErrorApuesta e) {
			model.addAttribute("estado", "error");
			return "apuestas/apuesta-post";
		}catch(ErrorTransaccion ex) {
			model.addAttribute("source", "confirmCheckout");
			model.addAttribute("apuesta", thisApuesta.get());
			model.put("error", ex.getMessage());
			return "apuestas/checkout-apuesta";
		}
	}
	
	/* Metodo de rechazo de la apuesta  */
	
	@PostMapping(value = {"/reject"})
	public String reject(@RequestParam(name = "idBet", required = false) String idBet,
			@RequestParam(name = "idUserB", required = false) String idUserB, ModelMap model) throws ErrorApuesta, ResponseStatusException, ErrorTransaccion {
		
		if(idBet == null || idBet.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		if(idUserB == null || idUserB.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		try {
			apuestaServicio.rechazarApuesta(idUserB, idBet);
			model.addAttribute("estado", "rejected");
			return "apuestas/apuesta-post";
		}catch(ErrorApuesta e) {
			model.addAttribute("estado", "error");
			return "apuestas/apuesta-post";
		}
	}
	
	/* Metodo que devuelve al usuario un resumen de la apuesta a la que acceda */
	
	@GetMapping(value = {"/summary/{id}", "/summary"})
	public String summary(@PathVariable("id") String id, ModelMap model) throws ResponseStatusException{
		
		if(id == null || id.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		Optional<Apuesta> thisApuesta = apuestaServicio.buscarPorId(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(thisApuesta.isPresent()) {
			Apuesta apuesta = thisApuesta.get();
			if(apuesta.getUsuarioA().getEmail().equals(auth.getName()) || (apuesta.getUsuarioB() != null && apuesta.getUsuarioB().getEmail().equals(auth.getName()))) {
				model.addAttribute("apuesta", apuesta);
				return "apuestas/resumen";
			}else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			}
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
	}

}
