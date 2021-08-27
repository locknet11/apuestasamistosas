package com.apuestasamistosas.app.controllers;

import com.apuestasamistosas.app.errors.ErrorEquipos;
import com.apuestasamistosas.app.errors.ErrorPremio;
import com.apuestasamistosas.app.errors.ErrorProveedores;
import com.apuestasamistosas.app.services.EquiposServicio;
import com.apuestasamistosas.app.services.EventosServicio;
import com.apuestasamistosas.app.services.PremioServicio;
import com.apuestasamistosas.app.services.ProveedoresServicio;
import com.apuestasamistosas.app.services.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @GetMapping("/panel")
    public String panel(ModelMap model) {
        model.addAttribute("cantUsuarios", usuarioServicio.contarTodos());
        model.addAttribute("cantProveedores", proveedoresServicio.contarTodos());
        model.addAttribute("cantPremios", premioServicio.contarTodos());
        model.addAttribute("cantEquipos", equiposServicio.contarTodos());
        return "/admin/index";
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
        return "/admin/cargas/equipos";
    }

    @GetMapping("/load/events")
    public String loadEventsView(ModelMap model) {
        model.addAttribute("equipos", equiposServicio.listarObjetos());
        return "/admin/cargas/eventos";
    }

    @GetMapping("/load/providers")
    public String loadProvidersView() {
        return "/admin/cargas/proveedores";
    }

    @GetMapping("/load/rewards")
    public String loadRewardsView(ModelMap model) {
        model.addAttribute("proveedores", proveedoresServicio.listarNombres());
        return "/admin/cargas/premios";
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
        
        return "/admin/cargas/equipos";
    }
    
//    @PostMapping("/load/events")
//    public String loadEvents(){
//        
//    }
    
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
        return "/admin/cargas/proveedores";
    }

    @PostMapping("/load/rewards")
    public String loadRewards(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "precio", required = false) Double precio,
            @RequestParam(name = "proveedor", required = false) String proveedor,
            ModelMap model) throws ErrorPremio{

        try{
            premioServicio.registroPremio(nombre, precio, proveedor);
            model.addAttribute("success", "Se ha cargado correctamente");
            model.addAttribute("proveedores", proveedoresServicio.listarNombres());
        }catch(ErrorPremio e){
            model.put("error", e.getMessage());
            model.addAttribute("nombre", nombre);
            model.addAttribute("precio", precio);
            model.addAttribute("proveedor", proveedor);
            model.addAttribute("proveedores", proveedoresServicio.listarNombres());
            return "/admin/cargas/premios";
        }
        return "/admin/cargas/premios";
    }
    

    
}

