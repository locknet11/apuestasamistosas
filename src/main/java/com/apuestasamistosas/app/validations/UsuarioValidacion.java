package com.apuestasamistosas.app.validations;

import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.errors.ErrorUsuario;
import com.apuestasamistosas.app.repositories.UsuarioRepositorio;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioValidacion {

    Logger logger = LoggerFactory.getLogger(UsuarioValidacion.class);

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    /*  el metodo validarDatos hace 5 validaciones basicas, que nombre y apellido no sea vacios 
        que el email no se encuentre previamente registrado, que las contraseñas coincidan,
        que el telefono ingresado tenga solo numeros y que el usuario sea mayor de edad.
    
        Existen 2 metodos de validacion, uno al momento de registrar y otro al momento de modificar.
     */
    
    
    public void validarDatos(String nombre, String apellido, String password,
            String passwordConfirmation, String email, String telefono,
            LocalDate fechaNacimiento) throws ErrorUsuario {


        /*  Validamos que tanto nombre como apellido no sean cadenas vacias */
        
        if (nombre == null || nombre.isEmpty()) {
            logger.error(ErrorUsuario.NO_NOMBRE);
            throw new ErrorUsuario(ErrorUsuario.NO_NOMBRE);
        }

        if (apellido == null || apellido.isEmpty()) {
            logger.error(ErrorUsuario.NO_APELLIDO);
            throw new ErrorUsuario(ErrorUsuario.NO_APELLIDO);
        }
        
        /*  Validamos que el email no sea nulo */
        
        if (email == null || email.isEmpty()){
            logger.error(ErrorUsuario.NO_EMAIL);
            throw new ErrorUsuario(ErrorUsuario.NO_EMAIL);
        } else {
            
            /* Validamos que el email no este previamente registrado */
            
            Optional<Usuario> checkEmail = usuarioRepositorio.findByEmail(email);
            
            if (checkEmail.isPresent()) {
                logger.error(ErrorUsuario.MAIL_REGIST);
                throw new ErrorUsuario(ErrorUsuario.MAIL_REGIST);
            }
        }
            
        
        if(password == null || password.isEmpty() || passwordConfirmation == null || passwordConfirmation.isEmpty()){
            logger.error(ErrorUsuario.NO_CLAVE);
            throw new ErrorUsuario(ErrorUsuario.NO_CLAVE);
        }else{
            /* Validamos que las contraseñas ingresadas sean iguales */
            
            if (!password.equals(passwordConfirmation)) {
                logger.error(ErrorUsuario.DIST_CLAVE);
                throw new ErrorUsuario(ErrorUsuario.DIST_CLAVE);
            }

            /* Validamos que la contraseña tenga al menos 8 caracteres. */
            
            if (password.length() < 8) {
                logger.error(ErrorUsuario.LONG_CLAVE);
                throw new ErrorUsuario(ErrorUsuario.LONG_CLAVE);
            }
        }

        /* Validamos que el telefono solo tenga numeros (maximo 13 digitos) y no este vacio */
        if (telefono == null || telefono.isEmpty()) {
            logger.error(ErrorUsuario.NO_TEL);
            throw new ErrorUsuario(ErrorUsuario.NO_TEL);
        } else {
            try {
                Long.parseLong(telefono);
                Integer numLength = telefono.length();
                if (numLength > 13) {
                    logger.error(ErrorUsuario.DIGIT_TEL);
                    throw new ErrorUsuario(ErrorUsuario.DIGIT_TEL);
                }
            } catch (NumberFormatException e) {
                logger.error(ErrorUsuario.DIGIT_TEL);
                throw new ErrorUsuario(ErrorUsuario.DIGIT_TEL);
            }
        }

        /*  Validamos que el usuario tenga entre 18 y 110 años */
        
        if (fechaNacimiento == null || fechaNacimiento.toString().isEmpty()){
            logger.error(ErrorUsuario.NO_DATE);
            throw new ErrorUsuario(ErrorUsuario.NO_DATE);
        }else{
            LocalDate hoy = LocalDate.now();

            Long edad = fechaNacimiento.until(hoy, ChronoUnit.YEARS);

            if (edad < 18 || edad > 110) {
                logger.error(ErrorUsuario.MAYOR_EDAD);
                throw new ErrorUsuario(ErrorUsuario.MAYOR_EDAD);
            }
        }
        

    }

    /* Validar datos al modificar */
    
       public void validarDatosModificar(String nombre, String apellido, String password,
            String passwordConfirmation, String telefono,
            LocalDate fechaNacimiento) throws ErrorUsuario {


        /*  Validamos que tanto nombre como apellido no sean cadenas vacias */
        if (nombre == null || nombre.isEmpty()) {
            logger.error(ErrorUsuario.NO_NOMBRE);
            throw new ErrorUsuario(ErrorUsuario.NO_NOMBRE);
        }

        if (apellido == null || apellido.isEmpty()) {
            logger.error(ErrorUsuario.NO_APELLIDO);
            throw new ErrorUsuario(ErrorUsuario.NO_APELLIDO);
        }
        
        
        if(password == null || password.isEmpty() || passwordConfirmation == null || passwordConfirmation.isEmpty()){
            logger.error(ErrorUsuario.NO_CLAVE);
            throw new ErrorUsuario(ErrorUsuario.NO_CLAVE);
        }else{
            /* Validamos que las contraseñas ingresadas sean iguales */
            
            if (!password.equals(passwordConfirmation)) {
                logger.error(ErrorUsuario.DIST_CLAVE);
                throw new ErrorUsuario(ErrorUsuario.DIST_CLAVE);
            }

            /* Validamos que la contraseña tenga al menos 8 caracteres. */
            
            if (password.length() < 8) {
                logger.error(ErrorUsuario.LONG_CLAVE);
                throw new ErrorUsuario(ErrorUsuario.LONG_CLAVE);
            }
        }

        /* Validamos que el telefono solo tenga numeros (maximo 13 digitos) y no este vacio */
        if (telefono == null || telefono.isEmpty()) {
            logger.error(ErrorUsuario.NO_TEL);
            throw new ErrorUsuario(ErrorUsuario.NO_TEL);
        } else {
            try {
                Integer numLength =telefono.length();
                if (numLength > 13) {
                    logger.error(ErrorUsuario.DIGIT_TEL);
                    throw new ErrorUsuario(ErrorUsuario.DIGIT_TEL);
                }
            } catch (NumberFormatException e) {
                logger.error(ErrorUsuario.DIGIT_TEL);
                throw new ErrorUsuario(ErrorUsuario.DIGIT_TEL);
            }
        }

        /*  Validamos que el usuario tenga entre 18 y 110 años */
        
        if (fechaNacimiento == null || fechaNacimiento.toString().isEmpty()){
            logger.error(ErrorUsuario.NO_DATE);
            throw new ErrorUsuario(ErrorUsuario.NO_DATE);
        }else{
            LocalDate hoy = LocalDate.now();

            Long edad = fechaNacimiento.until(hoy, ChronoUnit.YEARS);

            if (edad < 18 || edad > 110) {
                logger.error(ErrorUsuario.MAYOR_EDAD);
                throw new ErrorUsuario(ErrorUsuario.MAYOR_EDAD);
            }
        }

    }
    
    
}
