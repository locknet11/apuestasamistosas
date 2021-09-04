
package com.apuestasamistosas.app.controllers;

import com.apuestasamistosas.app.services.PremioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class GeneralController {
    
    @Autowired
    private PremioServicio premioServicio;
    
    @GetMapping
    public String index(){
        return "index";
    }
    
        
    @GetMapping("/perfil")
    public String perfil(){
        return "perfil";
    }
    
    @GetMapping(value = "/rewards")
    public String rewards(ModelMap model){
        if(premioServicio.listarTodos() != null){
            model.addAttribute("premios", premioServicio.listarTodos());
        }
        return "premios";
    }

}

