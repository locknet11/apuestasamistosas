
package com.apuestasamistosas.app.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bets")
@PreAuthorize("hasAnyRole('ROLE_USUARIO')")
public class ApuestaController {
    
    @GetMapping
    public String index(){
        return "apuestas/index";
    }
  

}
