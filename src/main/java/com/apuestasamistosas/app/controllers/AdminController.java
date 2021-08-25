package com.apuestasamistosas.app.controllers;

import com.apuestasamistosas.app.services.EquiposServicio;
import com.apuestasamistosas.app.services.EventosServicio;
import com.apuestasamistosas.app.services.PremioServicio;
import com.apuestasamistosas.app.services.ProveedoresServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private EventosServicio eventoServicio;

    @Autowired
    private PremioServicio premioServicio;

    @Autowired
    private ProveedoresServicio proveedoresServicio;

    @Autowired
    private EquiposServicio equiposServicio;

    @GetMapping("/panel")
    public String panel() {
        return "/admin/index";
    }

    @GetMapping
    public String index() {
        return "redirect:/admin/panel";
    }

    /*  Metodos relacionados a las cargas (POST y GET) */
    
    @GetMapping("/load")
    public String load() {
        return "redirect:/admin/panel";
    }

    @GetMapping("/load/teams")
    public String loadTeams() {
        return "/admin/cargas/equipos";
    }

    @GetMapping("/load/events")
    public String loadEvents() {
        return "/admin/cargas/eventos";
    }

    @GetMapping("/load/providers")
    public String loadProviders() {
        return "/admin/cargas/proveedores";
    }

    @GetMapping("/load/rewards")
    public String loadRewards() {
        return "/admin/cargas/premios";
    }

}
