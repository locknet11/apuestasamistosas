package com.apuestasamistosas.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apuestasamistosas.app.entities.Eventos;
import com.apuestasamistosas.app.errors.ErrorEventos;
import com.apuestasamistosas.app.services.EventosServicio;

@Controller
@RequestMapping("/events")
public class EventosController {

	@Autowired
	private EventosServicio eventoServicio;
	
	@GetMapping
	public String events(ModelMap model) {
		List<Eventos> listaEventos = eventoServicio.eventosOrdenadosPorFecha();
		if(!listaEventos.isEmpty()) {
			model.addAttribute("eventos", listaEventos);
		}else {
			model.put("error", ErrorEventos.NO_EVENTS);
		}
		return "eventos";
	}
	
	
	
}
