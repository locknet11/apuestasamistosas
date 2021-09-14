package com.apuestasamistosas.app.controllers;

import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.errors.ErrorUsuario;
import com.apuestasamistosas.app.repositories.ApuestaRepositorio;
import com.apuestasamistosas.app.services.ApuestaServicio;
import com.apuestasamistosas.app.services.UsuarioServicio;
import java.time.LocalDate;
import java.util.Optional;

import javax.mail.MessagingException;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private ApuestaServicio apuestaServicio;

    @GetMapping
    public String index() {
        return "redirect:/";
    }
    
    /*  Metodo GET para mostrar el form de registro */
    
    @GetMapping("/signup")
    public String signup(){
        /*  con el siguiente codigo nos encargamos de que al usuario que ya esta logueado
            sea redirigido al dashboard en caso de que acceda a la pagina de login
         */

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/user/dashboard";
        }
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
            @RequestParam(name = "archivo", required = false) MultipartFile archivo,
            ModelMap model) throws MessagingException, ErrorUsuario, Exception {
        try {
            if(archivo.getSize() >= 5000000){
                throw new ErrorUsuario(ErrorUsuario.MAX_SIZE);
            }
            usuarioServicio.registroUsuario(nombre, apellido, fechaNacimiento, provincia, localidad,
                    ciudad, calle, codigoPostal, password, passwordConfirmation, email, telefono, archivo);

        } catch (ErrorUsuario e) {
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
            model.put("archivo", archivo);

            return "signup";
        }
        model.addAttribute("email", email);
        return "confirmar-cuenta";
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
    
    /*  Metodo que recibe el codigo de confirmacion del usuario y activa la cuenta */
    
    @GetMapping("/confirm/{codConfirmacion}")
    public String confirm(@PathVariable String codConfirmacion){
        try{
            usuarioServicio.confirmarCuenta(codConfirmacion);
            return "cuenta-confirmada";
        }catch(ErrorUsuario e){
        	return "redirect:/error";
        }
    }
    
    /*  Metodo que autoriza unicamente a los usuarios registrados y logueados a acceder al dashboard */
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/dashboard")
    public String dashboard(){
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if((auth instanceof AnonymousAuthenticationToken)){
            return "redirect:/user/login";
        }else{
            return "dashboard";
        }
        
    }
    
    
    /* Metodo que recibe el email desde la vista y en caso de encontrar el usuario reenvia el correo*/
    
    @PostMapping("/resendEmail")
    public String resendEmail(ModelMap model, @RequestParam(name = "email", required = false) String email) throws ErrorUsuario{
        try{
            usuarioServicio.reenviarAccountConfirmation(email);
        }catch(ErrorUsuario e){
            model.put("error", e.getMessage());
        }
        model.addAttribute("email", email);
        return "confirmar-cuenta";
    }
    
    /* PERFIL USUARIO */
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/edit-profile")
    public String editProfile() {
    	return "editar-perfil";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @PostMapping("/edit-profile")
    public String editProfilePost(
    		@RequestParam(name = "id", required = false) String id,
    		@RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "apellido", required = false) String apellido,
            @RequestParam(name = "fechaNacimiento", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
            @RequestParam(name = "provincia", required = false) String provincia,
            @RequestParam(name = "ciudad", required = false) String ciudad,
            @RequestParam(name = "calle", required = false) String calle,
            @RequestParam(name = "codigoPostal", required = false) String codigoPostal,
            @RequestParam(name = "telefono", required = false) String telefono,
            @RequestParam(name = "archivo", required = false) MultipartFile archivo,
            ModelMap model
    		) throws ErrorUsuario, Exception{
    	try {
    		if(archivo != null && archivo.getSize() >= 5000000){
                throw new ErrorUsuario(ErrorUsuario.MAX_SIZE);
            }
    		usuarioServicio.modificarUsuario(id, nombre, apellido, fechaNacimiento, provincia, ciudad, calle, codigoPostal, telefono, archivo);
    		model.put("message", "Para ver los cambios, cerra sesión y volve a loguearte.");
    		return "editar-perfil";
    	}catch(ErrorUsuario e) {
    		model.put("error", e.getMessage());
    		return "editar-perfil";
    	}
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/profile")
    public String profile(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Usuario> thisUser = usuarioServicio.buscarPorEmail(auth.getName());
        if(thisUser.isPresent()) {
        	Usuario usuario = thisUser.get();
        	if (!apuestaServicio.listarApuestasConfirmadas(usuario.getId()).isEmpty()) {
				model.addAttribute("listaApuestasConfirmadas", apuestaServicio.listarApuestasConfirmadas(usuario.getId()));
			}
        	
        	if (!apuestaServicio.listarApuestasPendientes(usuario.getId()).isEmpty()) {
				model.addAttribute("listaApuestasPendientes", apuestaServicio.listarApuestasPendientes(usuario.getId()));
			}
        	
        }
    	return "perfil";
    }
    
    

}
