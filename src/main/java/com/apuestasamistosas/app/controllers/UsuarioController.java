package com.apuestasamistosas.app.controllers;

import com.apuestasamistosas.app.errors.ErrorUsuario;
import com.apuestasamistosas.app.services.UsuarioServicio;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public String index() {
        return "redirect:/";
    }
    
    /*  Metodo GET para mostrar el form de registro */
    
    @GetMapping("/signup")
    public String signup(){
        return "registro";
    }
    
    /*  metodo GET para resolver cuando un usuario apreta Refresh en un formulario fallido.
        Basicamente mapea al metodo signup el cual devuelve el form de registro
    */
    
    @GetMapping("/register")
    public String registerGet(){
        return "redirect:/user/signup";
    }

    /* Metodo que recibe la informacion del formulario y llama al metodo registroUsuario
       para persistir los datos. 
     */
    
    @PostMapping("/register")
    public String registerPost(
            @RequestParam(name = "nombre") String nombre,
            @RequestParam(name = "apellido") String apellido,
            @RequestParam(name = "fechaNacimiento") LocalDate fechaNacimiento,
            @RequestParam(name = "provincia") String provincia,
            @RequestParam(name = "localidad") String localidad,
            @RequestParam(name = "ciudad") String ciudad,
            @RequestParam(name = "calle") String calle,
            @RequestParam(name = "codigoPostal") String codigoPostal,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "passwordConfirmation") String passwordConfirmation,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "telefono") String telefono) {
        try {
            usuarioServicio.registroUsuario(nombre, apellido, fechaNacimiento, provincia, localidad,
                    ciudad, calle, codigoPostal, password, passwordConfirmation, email, telefono);
        } catch (ErrorUsuario e) {
            System.out.println("Error al registrar al usuario");
            System.out.println(e);
            return "registro";
        }
        return "index";
    }
    
    /*  Metodo que devuelve la pagina de login  */
    
    @GetMapping("/login")
    public String login(ModelMap model, @RequestParam(name = "error", required = false) String error){
        if(error != null){
            model.put("error", "Usuario o contraseña incorrectos.");
        }
        
        return "login";
    }

}
