package com.apuestasamistosas.app.validations;

import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.errors.ErrorUsuario;
import com.apuestasamistosas.app.repositories.UsuarioRepositorio;
import com.apuestasamistosas.app.services.UsuarioServicio;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UsuarioValidacion {

    Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);

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

        Optional<Usuario> checkEmail = usuarioRepositorio.findByEmail(email);

        /*  Validamos que tanto nombre como apellido no sean cadenas vacias */
        if (nombre.isEmpty()) {
            logger.error(ErrorUsuario.NO_NOMBRE);
            throw new ErrorUsuario(ErrorUsuario.NO_NOMBRE);
        }

        if (apellido.isEmpty()) {
            logger.error(ErrorUsuario.NO_APELLIDO);
            throw new ErrorUsuario(ErrorUsuario.NO_APELLIDO);
        }
        
        /*  Validamos que el email no sea nulo */
        
        if (email.isEmpty()){
            logger.error(ErrorUsuario.NO_EMAIL);
            throw new ErrorUsuario(ErrorUsuario.NO_EMAIL);
        }

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

        /* Validamos que la contraseña tenga al menos 8 caracteres. */
        if (password.length() < 8) {
            logger.error(ErrorUsuario.LONG_CLAVE);
            throw new ErrorUsuario(ErrorUsuario.LONG_CLAVE);
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

    /* Validar datos al modificar */
    
    public void validarDatosModificar(String nombre, String apellido, String password,
            String passwordConfirmation, String telefono,
            LocalDate fechaNacimiento) throws ErrorUsuario {

        /*  Validamos que tanto nombre como apellido no sean cadenas vacias */
        if (nombre.isEmpty()) {
            logger.error(ErrorUsuario.NO_NOMBRE);
            throw new ErrorUsuario(ErrorUsuario.NO_NOMBRE);
        }

        if (apellido.isEmpty()) {
            logger.error(ErrorUsuario.NO_APELLIDO);
            throw new ErrorUsuario(ErrorUsuario.NO_APELLIDO);
        }

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
    
    
}
