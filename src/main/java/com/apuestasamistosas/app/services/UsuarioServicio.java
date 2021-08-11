package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.errors.ErrorUsuario;
import com.apuestasamistosas.app.repositories.UsuarioRepositorio;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    /*  Logger que lleva el registro de cada transaccion  */
    
    Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);

    /*  el metodo validarDatos hace 4 validaciones basicas, 
        que el email no se encuentre previamente registrado, que las contraseñas coincidan,
        que el telefono ingresado tenga solo numeros y que el usuario sea mayor de edad.
     */
    
    public void validarDatos(String password, String passwordConfirmation, String email,
            String telefono, LocalDate fechaNacimiento) throws ErrorUsuario {

        Optional<Usuario> checkEmail = usuarioRepositorio.findByEmail(email);

        /* Validamos que el email no este previamente registrado */
        
        if (checkEmail.isPresent()) {
            logger.error(ErrorUsuario.MAIL_REGIST);
            throw new ErrorUsuario(ErrorUsuario.MAIL_REGIST);
        }

        /* Validamos que las contraseñas ingresadas sean iguales */
        
        if (!password.equals(passwordConfirmation)) {
            logger.error(ErrorUsuario.DIST_CLAVE);
            throw new ErrorUsuario(ErrorUsuario.DIST_CLAVE);
        }

        /* Validamos que el telefono solo tenga numeros (maximo 13 digitos) y no este vacio */
        
        if (telefono.isEmpty()) {
            logger.error(ErrorUsuario.NO_TEL);
            throw new ErrorUsuario(ErrorUsuario.NO_TEL);
        } else {
            try {
                Long num = Long.parseLong(telefono);
                if (num > 13) {
                    logger.error(ErrorUsuario.DIGIT_TEL);
                    throw new ErrorUsuario(ErrorUsuario.DIGIT_TEL);
                }
            } catch (NumberFormatException e) {
                logger.error(ErrorUsuario.DIGIT_TEL);
                throw new ErrorUsuario(ErrorUsuario.DIGIT_TEL);
            }
        }

        /*  Validamos que el usuario tenga entre 18 y 110 años */
        
        LocalDate hoy = LocalDate.now();

        Long edad = fechaNacimiento.until(hoy, ChronoUnit.YEARS);

        if (edad < 18 || edad > 110) {
            logger.error(ErrorUsuario.MAYOR_EDAD);
            throw new ErrorUsuario(ErrorUsuario.MAYOR_EDAD);
        }

    }

    /* Metodo de registro del usuario */
    
    @Transactional
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
    
    @Transactional
    public void modificarUsuario(String id, String nombre, String apellido, LocalDate fechaNacimiento, String provincia,
            String localidad, String ciudad, String calle, String codigoPostal,
            String password, String passwordConfirmation, String email, String telefono) throws ErrorUsuario {

        validarDatos(password, passwordConfirmation, email, telefono, fechaNacimiento);

        Optional<Usuario> thisUser = usuarioRepositorio.findById(id);

        if (thisUser.isPresent()) {
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

        } else {
            logger.error(ErrorUsuario.NO_EXISTE);
            throw new ErrorUsuario(ErrorUsuario.NO_EXISTE);
        }

    }

    /*  Metodo para dar de baja la cuenta  */
    
    // aca va el metodo
    
    /*  Metodo de login del usuario, si el usuario no existe o esta dado de baja va retornar null */
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> thisUser = usuarioRepositorio.findByEmail(email);

        if (thisUser.isPresent()) {

            Usuario usuario = thisUser.get();

            if (usuario.isAlta()) {
                List<GrantedAuthority> permisos = new ArrayList<>();

                /*  Definicion de los permisos */
                
                GrantedAuthority p1 = new SimpleGrantedAuthority("USUARIO");
                GrantedAuthority p2 = new SimpleGrantedAuthority("APUESTA");
                permisos.add(p1);
                permisos.add(p2);

                User user = new User(usuario.getEmail(), usuario.getPassword(), permisos);

                return user;

            } else {
                logger.error(ErrorUsuario.NO_ACTIVO);
                throw new UsernameNotFoundException(ErrorUsuario.NO_ACTIVO);
            }
        } else {
            logger.error(ErrorUsuario.NO_EXISTE);
            throw new UsernameNotFoundException(ErrorUsuario.NO_EXISTE);
        }

    }

}
