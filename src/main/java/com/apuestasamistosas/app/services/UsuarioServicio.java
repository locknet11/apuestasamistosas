package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.errors.ErrorUsuario;
import com.apuestasamistosas.app.repositories.UsuarioRepositorio;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    /*  el metodo validarDatos hace 4 validaciones basicas, 
        que el email no se encuentre previamente registrado, que las contraseñas coincidan,
        que el telefono ingresado tenga solo numeros y que el usuario sea mayor de edad.
     */
    
    public void validarDatos(String password, String passwordConfirmation, String email,
            String telefono, LocalDate fechaNacimiento) throws ErrorUsuario {

        Optional<Usuario> checkEmail = usuarioRepositorio.findByEmail(email);

        /* Validamos que el email no este previamente registrado */
        
        if (checkEmail.isPresent()) {
            throw new ErrorUsuario("Este correo ya se encuentra registrado.");
        }

        /* Validamos que las contraseñas ingresadas sean iguales */
        
        if (!password.equals(passwordConfirmation)) {
            throw new ErrorUsuario("Las claves no coinciden.");
        }

        /* Validamos que el telefono solo tenga numeros (maximo 13 digitos) y no este vacio */
        
        if (telefono.isEmpty()) {
            throw new ErrorUsuario("Debe ingresar un numero telefónico.");
        } else {
            try {
                Long num = Long.parseLong(telefono);
                if (num > 13) {
                    throw new ErrorUsuario("Solo pueden ser numeros (max. 13)");
                }
            } catch (NumberFormatException e) {
                throw new ErrorUsuario("Solo pueden ser numeros (max. 13)");
            }
        }

        /*  Validamos que el usuario tenga entre 18 y 110 años */
        
        LocalDate hoy = LocalDate.now();

        Long edad = fechaNacimiento.until(hoy, ChronoUnit.YEARS);

        if (edad < 18 || edad > 110) {
            throw new ErrorUsuario("Tiene que ser mayor de edad para poder registrarse.");
        }

    }

    /* Metodo de registro del usuario */
    
    public void registroUsuario(String nombre, String apellido, LocalDate fechaNacimiento, String provincia,
            String localidad, String ciudad, String calle, String codigoPostal,
            String password, String passwordConfirmation, String email, String telefono) throws ErrorUsuario {

        validarDatos(password, passwordConfirmation, email, telefono, fechaNacimiento);
        String encoded_password = new BCryptPasswordEncoder().encode(password);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setAlta(true);
        usuario.setFechaNacimiento(fechaNacimiento);
        usuario.setProvincia(provincia);
        usuario.setLocalidad(localidad);
        usuario.setCiudad(ciudad);
        usuario.setCalle(calle);
        usuario.setCodigoPostal(codigoPostal);
        usuario.setPassword(encoded_password);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);

        usuarioRepositorio.save(usuario);

    }

    /*  Metodo de modificacion del usuario */
    
    public void modificarUsuario(String id, String nombre, String apellido, LocalDate fechaNacimiento, String provincia,
            String localidad, String ciudad, String calle, String codigoPostal,
            String password, String passwordConfirmation, String email, String telefono) throws ErrorUsuario {

        validarDatos(password, passwordConfirmation, email, telefono, fechaNacimiento);

        Optional<Usuario> thisUser = usuarioRepositorio.findById(id);
        
        
        if (thisUser.isPresent()){
            String encoded_password = new BCryptPasswordEncoder().encode(password);
            Usuario usuario = thisUser.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setAlta(true);
            usuario.setFechaNacimiento(fechaNacimiento);
            usuario.setProvincia(provincia);
            usuario.setLocalidad(localidad);
            usuario.setCiudad(ciudad);
            usuario.setCalle(calle);
            usuario.setCodigoPostal(codigoPostal);
            usuario.setPassword(encoded_password);
            usuario.setEmail(email);
            usuario.setTelefono(telefono);

            usuarioRepositorio.save(usuario);
            
        }else{
            throw new ErrorUsuario("Este usuario no existe");
        }
            
    }
    
    /*  Metodo de login del usuario */

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> thisUser = usuarioRepositorio.findByEmail(email);
        
        if(thisUser.isPresent()){
            
            Usuario usuario = thisUser.get();
            List<GrantedAuthority> permisos = new ArrayList<>();
            
            /*  Definicion de los permisos */
            
            GrantedAuthority p1 = new SimpleGrantedAuthority("USUARIO");
            GrantedAuthority p2 = new SimpleGrantedAuthority("APUESTA");
            permisos.add(p1);
            permisos.add(p2);
                       
            User user = new User(usuario.getEmail(), usuario.getPassword(), permisos);
            
            return user;
        }else{
            return null;
        }
        
    }

}
