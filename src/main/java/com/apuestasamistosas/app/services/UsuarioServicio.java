package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Foto;
import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.errors.ErrorUsuario;
import com.apuestasamistosas.app.repositories.FotoRepositorio;
import com.apuestasamistosas.app.repositories.UsuarioRepositorio;
import com.apuestasamistosas.app.validations.UsuarioValidacion;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.mail.Multipart;
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
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private FotoServicio fotoServicio;
    
    @Autowired
    UsuarioValidacion uv;
    
    /*  Logger que lleva el registro de cada transaccion  */
    
    Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);
    

    /* Metodo de registro del usuario */
    
    @Transactional
    public void registroUsuario(MultipartFile archivo,String nombre, String apellido, LocalDate fechaNacimiento, String provincia,
            String localidad, String ciudad, String calle, String codigoPostal,
            String password, String passwordConfirmation, String email, String telefono) throws ErrorUsuario, Exception {

        uv.validarDatos(nombre, apellido, password, passwordConfirmation, email, telefono, fechaNacimiento);
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
        
        Foto foto = fotoServicio.guardar(archivo);
        usuario.setFoto(foto);

        usuarioRepositorio.save(usuario);

    }

    /*  Metodo de modificacion del usuario */
    
    @Transactional
    public void modificarUsuario(MultipartFile archivo,String id, String nombre, String apellido, LocalDate fechaNacimiento, String provincia,
            String localidad, String ciudad, String calle, String codigoPostal,
            String password, String passwordConfirmation, String telefono) throws ErrorUsuario, Exception {

        Optional<Usuario> thisUser = usuarioRepositorio.findById(id);
        
        uv.validarDatosModificar(nombre, apellido, password, passwordConfirmation, telefono, fechaNacimiento);

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
            usuario.setTelefono(telefono);
            String idFoto=null;
            if (usuario.getFoto()!=null) {
                idFoto=usuario.getFoto().getId();
            }
            Foto foto = fotoServicio.actualizar(idFoto,archivo);
            usuario.setFoto(foto);
            usuarioRepositorio.save(usuario);

        } else {
            logger.error(ErrorUsuario.NO_EXISTE);
            throw new ErrorUsuario(ErrorUsuario.NO_EXISTE);
        }

    }

    /*  Metodo para dar de baja la cuenta  */
    
    @Transactional
    public void bajaUsuario(String id) throws ErrorUsuario {
        Optional<Usuario> thisUser = usuarioRepositorio.findById(id);

        if (thisUser.isPresent()) {
            Usuario usuario = thisUser.get();
            usuario.setAlta(false);
            usuarioRepositorio.save(usuario);
        } else {
            logger.error(ErrorUsuario.NO_EXISTE);
            throw new ErrorUsuario(ErrorUsuario.NO_EXISTE);
        }
    }
    
    /*  Metodo para dar de alta la cuenta */

    @Transactional
    public void altaUsuario(String id) throws ErrorUsuario {
        Optional<Usuario> thisUser = usuarioRepositorio.findById(id);

        if (thisUser.isPresent()) {
            Usuario usuario = thisUser.get();
            usuario.setAlta(true);
            usuarioRepositorio.save(usuario);
        } else {
            logger.error(ErrorUsuario.NO_EXISTE);
            throw new ErrorUsuario(ErrorUsuario.NO_EXISTE);
        }
    }
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

                logger.info("Se ha loggeado el user: " + usuario.getEmail());
                
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
