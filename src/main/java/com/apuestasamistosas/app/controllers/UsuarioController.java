package com.apuestasamistosas.app.controllers;

import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.errors.ErrorUsuario;
import com.apuestasamistosas.app.services.UsuarioServicio;
import java.time.LocalDate;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        return "signup";
    }
    
    /*  metodo GET para resolver cuando un usuario apreta Refresh en un formulario fallido.
        Basicamente mapea al metodo signup el cual devuelve el form de registro
    */
    
    @GetMapping("/register")
    public String registerGet(){
        return "redirect:/user/signup";
    }

    /* Metodo que recibe la informacion del formulario y llama al metodo registroUsuario
       para persistir los datos. Por defecto ningun parametro es requerido porque la validacion
       se realiza enviando la informacion a la clase UsuarioValidacion, si la informacion no llega
       en el estado esperado se controla cada una de las excepciones. Para el usuario es transparente
       y es mucho mas seguro ya que si del lado del cliente se altera la estructura del formulario
       no va repercutir en la BD ni tampoco generar excepciones no controladas. 
     */
    
    @PostMapping("/signup")
    public String registerPost(
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
            @RequestParam(name = "telefono", required = false) String telefono,
            ModelMap model) throws MessagingException, ErrorUsuario {
        try {
            usuarioServicio.registroUsuario(nombre, apellido, fechaNacimiento, provincia, localidad,
                    ciudad, calle, codigoPostal, password, passwordConfirmation, email, telefono);
        } catch (ErrorUsuario e) {
            System.out.println(e);
            
            model.addAttribute("error", e.getMessage());
            model.put("nombre", nombre);
            model.put("apellido", apellido);
            model.put("fechaNacimiento", fechaNacimiento);
            model.put("provincia", provincia);
            model.put("localidad", localidad);
            model.put("ciudad", ciudad);
            model.put("calle", calle);
            model.put("codigoPostal", codigoPostal);
            model.put("password", password);
            model.put("passwordConfirmation", passwordConfirmation);
            model.put("telefono", telefono);
            model.put("email", email);
            
            return "signup";
        }
        return "redirect:/";
    }
    
    /*  Metodo que devuelve la pagina de login  */
    
    @GetMapping("/login")
    public String login(ModelMap model, @RequestParam(name = "error", required = false) String error) throws ErrorUsuario{
       
        if(error != null){
            model.put("error", "Usuario o contraseña incorrectos.");
        }
        
        
        /*  con el siguiente codigo nos encargamos de que al usuario que ya esta logueado
            sea redirigido al dashboard en caso de que acceda a la pagina de login
        */
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if(!(auth instanceof AnonymousAuthenticationToken)){
            return "redirect:/user/dashboard";
        }
        
        return "login";
    }
    
    @GetMapping("/confirm/{codConfirmacion}")
    public String confirm(@PathVariable String codConfirmacion){
        try{
            usuarioServicio.confirmarCuenta(codConfirmacion);
            return "cuentaconfirmada";
        }catch(ErrorUsuario e){
            return "codinvalido";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }

}
