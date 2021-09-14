
package com.apuestasamistosas.app.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apuestasamistosas.app.entities.Premio;
import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.services.PremioServicio;
import com.apuestasamistosas.app.services.UsuarioServicio;

@Controller
@RequestMapping("/")
public class GeneralController {
	
	@Autowired
	private UsuarioServicio usuarioServicio;
	
	@Autowired
	private PremioServicio premioServicio;
    
    @GetMapping
    public String index(){
        return "index";
    }
    
    @GetMapping("/legal")
    public String legal() {
    	return "legal";
    }
    
    @GetMapping("faqs")
    public String faqs() {
    	return "faqs";
    }
    
    @GetMapping("/ranking")
    public String ranking(ModelMap model) {
    	
    	List<Premio> thisListPremio = premioServicio.listarPorRanking();
    	List<Usuario> thisListUsuario = usuarioServicio.listarPorGanados();
    	
    	if(!thisListPremio.isEmpty()) {
    		model.addAttribute("premios", thisListPremio);
    	}
    	
    	if(!thisListUsuario.isEmpty()) {
    		model.addAttribute("usuarios", thisListUsuario);
    	}
    	
    	return "ranking";
    }
    
    

}

