
package com.apuestasamistosas.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class GeneralController {
    
    @GetMapping()
    public String index(){
        return "rama santiago";
    }

}
