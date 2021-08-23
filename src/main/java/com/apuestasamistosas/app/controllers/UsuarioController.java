package com.apuestasamistosas.app.controllers;

import com.apuestasamistosas.app.errors.ErrorUsuario;
import com.apuestasamistosas.app.services.UsuarioServicio;
import java.time.LocalDate;
import java.time.Month;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    public String signup() {
        return "registro";
    }

    /*  metodo GET para resolver cuando un usuario apreta Refresh en un formulario fallido.
        Basicamente mapea al metodo signup el cual devuelve el form de registro
     */
    @GetMapping("/register")
    public String registerGet() {
        return "redirect:/user/signup";
    }

    /* Metodo que recibe la informacion del formulario y llama al metodo registroUsuario
       para persistir los datos. Por defecto ningun parametro es requerido porque la validacion
       se realiza enviando la informacion a la clase UsuarioValidacion, si la informacion no llega
       en el estado esperado se controla cada una de las excepciones. Para el usuario es transparente
       y es mucho mas seguro ya que si del lado del cliente se altera la estructura del formulario
       no va repercutir en la BD ni tampoco generar excepciones no controladas. 
     */
    @PostMapping("/register")
    public String registerPost(
            
            MultipartFile archivo,
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "apellido", required = false) String apellido,
            @RequestParam(name = "fechaNacimiento", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
            @RequestParam(name = "provincia", required = false) String provincia,
            @RequestParam(name = "localidad", required = false) String localidad,
            @RequestParam(name = "ciudad", required = false) String ciudad,
            @RequestParam(name = "calle", required = false) String calle,
            @RequestParam(name = "codigoPostal", required = false) String codigoPostal,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "passwordConfirmation", required = false) String passwordConfirmation,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "telefono", required = false) String telefono) throws Exception {
        try {

            usuarioServicio.registroUsuario(archivo, nombre, apellido, fechaNacimiento, provincia, localidad,
                    ciudad, calle, codigoPostal, password, passwordConfirmation, email, telefono);
        } catch (ErrorUsuario e) {
          
            return "registro";
        }
        
        return "index";
    }

    /*  Metodo que devuelve la pagina de login  */
    @GetMapping("/login")
    public String login(ModelMap model, @RequestParam(name = "error", required = false) String error) {
        if (error != null) {
            model.put("error", "Usuario o contraseña incorrectos.");
        }

        return "login";
    }

}
