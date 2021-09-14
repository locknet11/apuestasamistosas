package com.apuestasamistosas.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apuestasamistosas.app.entities.Premio;
import com.apuestasamistosas.app.errors.ErrorPremio;
import com.apuestasamistosas.app.services.PremioServicio;

@Controller
@RequestMapping("/rewards")
public class PremioController {
	
	@Autowired
    private PremioServicio premioServicio;
	
    @GetMapping
    public String rewards(ModelMap model){
    	List<Premio> thisList = premioServicio.listarTodos();
        if(!thisList.isEmpty()){
            model.addAttribute("premios", premioServicio.listarTodos());
        }else {
        	model.put("error", ErrorPremio.NO_REWARDS);
        }
        return "premios";
    }
    

}
