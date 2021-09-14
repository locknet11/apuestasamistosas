package com.apuestasamistosas.app.controllers;

import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.errors.ErrorEquipos;
import com.apuestasamistosas.app.errors.ErrorEventos;
import com.apuestasamistosas.app.errors.ErrorPremio;
import com.apuestasamistosas.app.errors.ErrorProveedores;
import com.apuestasamistosas.app.errors.ErrorUsuario;
import com.apuestasamistosas.app.services.ApuestaServicio;
import com.apuestasamistosas.app.services.EquiposServicio;
import com.apuestasamistosas.app.services.EventosServicio;
import com.apuestasamistosas.app.services.PremioServicio;
import com.apuestasamistosas.app.services.ProveedoresServicio;
import com.apuestasamistosas.app.services.UsuarioServicio;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private ApuestaServicio apuestaServicio;
    
    @GetMapping("/panel")
    public String panel(ModelMap model) {
        model.addAttribute("cantUsuarios", usuarioServicio.contarTodos());
        model.addAttribute("cantProveedores", proveedoresServicio.contarTodos());
        model.addAttribute("cantPremios", premioServicio.contarTodos());
        model.addAttribute("cantEquipos", equiposServicio.contarTodos());
        model.addAttribute("cantEventos", eventoServicio.contarTodos());
        model.addAttribute("cantApuestas", apuestaServicio.contarTodos());
        return "admin/index";
    }

    @GetMapping
    public String index() {
        return "redirect:/admin/panel";
    }

    /*  Metodos GET relacionados a las cargas */
    
    @GetMapping("/load")
    public String load() {
        return "redirect:/admin/panel";
    }

    @GetMapping("/load/teams")
    public String loadTeamsView() {
        return "admin/cargas/equipos";
    }

    @GetMapping("/load/events")
    public String loadEventsView(ModelMap model) {
        model.addAttribute("equipos", equiposServicio.listarObjetos());
        model.addAttribute("fechaMinima", LocalDate.now().plusDays(2));
        return "admin/cargas/eventos";
    }

    @GetMapping("/load/providers")
    public String loadProvidersView() {
        return "admin/cargas/proveedores";
    }

    @GetMapping("/load/rewards")
    public String loadRewardsView(ModelMap model) {
        model.addAttribute("proveedores", proveedoresServicio.listarNombres());
        return "admin/cargas/premios";
    }
    
    /*  Metodos POST relacionados a las cargas  */
    
    @PostMapping("/load/teams")
    public String loadTeams(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "deporte", required = false) String deporte,
            @RequestParam(name = "archivo", required = false) MultipartFile archivo,
            ModelMap model
    ) throws ErrorEquipos, Exception {
        try {
            equiposServicio.registroEquipo(nombre, deporte, archivo);
            model.addAttribute("success", "Se ha cargado correctamente");
        } catch (ErrorEquipos e) {
            model.put("error", e.getMessage());
            model.addAttribute("nombre", nombre);
            model.addAttribute("deporte", deporte);
            model.addAttribute("archivo", archivo);
        }
        
        return "admin/cargas/equipos";
    }
    
    @PostMapping("/load/events")
    public String loadEvents(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "equipoA", required = false) String equipoA,
            @RequestParam(name = "equipoB", required = false) String equipoB,
            @RequestParam(name = "fechaEvento", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEvento,
            @RequestParam(name = "horaEvento", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaEvento,
            ModelMap model
    ) throws ErrorEventos{
        try{
            eventoServicio.registroEvento(nombre, equipoA, equipoB, fechaEvento, horaEvento);
            model.addAttribute("success", "Se ha cargado correctamente");
            model.addAttribute("fechaMinima", LocalDate.now().plusDays(2));
        }catch(ErrorEventos e){
            model.put("error", e.getMessage());
            model.addAttribute("equipos", equiposServicio.listarObjetos());
            model.addAttribute("nombre", nombre);
            model.addAttribute("equipoA", equipoA);
            model.addAttribute("equipoB", equipoB);
            model.addAttribute("fechaEvento", fechaEvento);
            model.addAttribute("horaEvento", horaEvento);
            model.addAttribute("fechaMinima", LocalDate.now().plusDays(2));
        }
        model.addAttribute("equipos", equiposServicio.listarObjetos());
        return "admin/cargas/eventos";
    }
    
    @PostMapping("/load/providers")
    public String loadProviders(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "provincia", required = false) String provincia,
            @RequestParam(name = "ciudad", required = false) String ciudad,
            @RequestParam(name = "calle", required = false) String calle,
            @RequestParam(name = "codigoPostal", required = false) String codigoPostal,
            @RequestParam(name = "telefono", required = false) String telefono,
            @RequestParam(name = "responsable", required = false) String responsable,
            ModelMap model
    ) throws ErrorProveedores{
        try {
            proveedoresServicio.registroProveedor(nombre, provincia, ciudad, calle, codigoPostal, telefono, responsable);
            model.addAttribute("success", "Se ha cargado correctamente");
        } catch (ErrorProveedores e) {
            model.put("error", e.getMessage());
            model.addAttribute("nombre", nombre);
            model.addAttribute("provincia", provincia);
            model.addAttribute("ciudad", ciudad);
            model.addAttribute("calle", calle);
            model.addAttribute("codigoPostal", codigoPostal);
            model.addAttribute("telefono", telefono);
            model.addAttribute("responsable", responsable);
        }
        return "admin/cargas/proveedores";
    }

    @PostMapping("/load/rewards")
    public String loadRewards(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "precio", required = false) Double precio,
            @RequestParam(name = "proveedor", required = false) String proveedor,
            @RequestParam(name = "archivo", required = false) MultipartFile archivo,
            ModelMap model) throws ErrorPremio, Exception{

        try{
            premioServicio.registroPremio(nombre, precio, proveedor, archivo);
            model.addAttribute("success", "Se ha cargado correctamente");
            model.addAttribute("proveedores", proveedoresServicio.listarNombres());
        }catch(ErrorPremio e){
            model.put("error", e.getMessage());
            model.addAttribute("nombre", nombre);
            model.addAttribute("precio", precio);
            model.addAttribute("proveedor", proveedor);
            model.addAttribute("proveedores", proveedoresServicio.listarNombres());
            return "admin/cargas/premios";
        }
        return "admin/cargas/premios";
    }
    

    /* 	AÑADIR ADMIN   */
    
    @GetMapping("/new-admin")
    public String newAdmin(ModelMap model) {
    	List<Usuario> adminUsers = usuarioServicio.listarAdmins();
    	if(!adminUsers.isEmpty()) {
    		model.addAttribute("admins", adminUsers);
    		return "admin/nuevo-admin";
    	}else {
    		model.put("error", "Ha ocurrido un error");
    		return "admin/nuevo-admin";
    	}
    	
    }
    
	@PostMapping("/new-admin")
	public String addNewAdmin(@RequestParam(name = "email", required = false) String email, ModelMap model) {
		
		List<Usuario> adminUsers = usuarioServicio.listarAdmins();
		
		if (email == null || email.isEmpty()) {
			model.put("error", ErrorUsuario.NO_EMAIL);
			return "admin/nuevo-admin";
		}

		try {
			usuarioServicio.altaAdmin(email);
			return "redirect:/admin/new-admin";
		} catch (ErrorUsuario e) {
			model.put("error", e.getMessage());
			model.addAttribute("admins", adminUsers);
			return "admin/nuevo-admin";
		}

	}
    
}

