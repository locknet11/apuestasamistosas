package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.errors.ErrorUsuario;
import com.apuestasamistosas.app.repositories.UsuarioRepositorio;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    /*  el metodo validarDatos hace 3 validaciones basicas, 
        que el email no se encuentre previamente registrado, que las contraseñas coincidan,
        y que el telefono ingresado tenga solo numeros.
     */
    public void validarDatos(String password, String passwordConfirmation, String email, String telefono) throws ErrorUsuario {

        Optional<Usuario> checkEmail = usuarioRepositorio.findByEmail(email);

        /* Validamos que el email no este previamente registrado */
        if (checkEmail.isPresent()) {
            throw new ErrorUsuario("Este correo ya se encuentra registrado.");
        }

        /* Validamos que las contraseñas ingresadas sean iguales */
        if (!password.equals(passwordConfirmation)) {
            throw new ErrorUsuario("Las claves no coinciden.");
        }

        /* Validamos que el telefono solo tenga numeros y no este vacio */
        
        if (telefono == null) {
            throw new ErrorUsuario("Debe ingresar un numero telefónico.");
        } else {
            try {
                Long.parseLong(telefono);
            } catch (NumberFormatException e) {
                throw new ErrorUsuario("Solo pueden ser numeros");
            }
        }

    }

    /* Metodo de registro del usuario */
    public void registroUsuario(String nombre, String apellido, LocalDate fechaNacimiento , String provincia,
            String localidad, String ciudad, String calle, String codigoPostal,
            String password, String passwordConfirmation, String email, String telefono) throws ErrorUsuario {

        validarDatos(password, passwordConfirmation, email, telefono);
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

}
