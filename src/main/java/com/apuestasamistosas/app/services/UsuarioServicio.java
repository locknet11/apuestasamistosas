package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Foto;
import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.errors.ErrorUsuario;
import com.apuestasamistosas.app.repositories.UsuarioRepositorio;
import com.apuestasamistosas.app.utilities.RandomGenerator;
import com.apuestasamistosas.app.validations.UsuarioValidacion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private UsuarioValidacion uv;

    @Autowired
    private MailServicio mailServicio;
    
    @Autowired
    private FotoServicio fotoServicio;

    /*  Logger que lleva el registro de cada transaccion  */
    Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);


    /* Metodo de registro del usuario */
    @Transactional
    public void registroUsuario(String nombre, String apellido, LocalDate fechaNacimiento, String provincia,
            String localidad, String ciudad, String calle, String codigoPostal,
            String password, String passwordConfirmation, String email, String telefono, MultipartFile archivo) throws ErrorUsuario, Exception {

        uv.validarDatos(nombre, apellido, password, passwordConfirmation, email, telefono, fechaNacimiento);
        String encoded_password = new BCryptPasswordEncoder().encode(password);

        Foto foto = fotoServicio.guardar(archivo);
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
        usuario.setCodConfirmacion(RandomGenerator.generateUUID());
        usuario.setConfirmado(false);
        usuario.setAdmin(false);
        if(foto != null){
            usuario.setFoto(foto);
        }
        

        usuarioRepositorio.save(usuario);

        mailServicio.accountConfirmation(usuario);

    }
    
    /*  Metodo para reenviar el link de confirmacion */
    
    public void reenviarAccountConfirmation(String email) throws ErrorUsuario{
        Optional<Usuario> thisUser = usuarioRepositorio.findByEmail(email);
        if(thisUser.isPresent()){
            Usuario usuario = thisUser.get();
            mailServicio.accountConfirmation(usuario);
        }else{
            throw new ErrorUsuario(ErrorUsuario.NO_EXISTE);
        }
    }

    /*  Metodo de modificacion del usuario */
    @Transactional
    public void modificarUsuario(String id, String nombre, String apellido, LocalDate fechaNacimiento, String provincia,
           String ciudad, String calle, String codigoPostal, String telefono, MultipartFile archivo) throws ErrorUsuario, Exception {
       
        Optional<Usuario> thisUser = usuarioRepositorio.findById(id);

        uv.validarDatosModificar(nombre, apellido, telefono, fechaNacimiento);

        if (thisUser.isPresent()) {
            Usuario usuario = thisUser.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setAlta(true);
            usuario.setFechaNacimiento(fechaNacimiento);
            usuario.setProvincia(provincia);
            usuario.setCiudad(ciudad);
            usuario.setCalle(calle);
            usuario.setCodigoPostal(codigoPostal);
            usuario.setTelefono(telefono);
            
            if(archivo != null) {
                if (usuario.getFoto() != null) {
                    String idFoto = usuario.getFoto().getId();
                    Foto foto = fotoServicio.actualizar(idFoto, archivo);
                    usuario.setFoto(foto);

                }else {
                	Foto foto = fotoServicio.guardar(archivo);
                    usuario.setFoto(foto);
                }
            }
            
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

    /*  Metodo para confirmar cuenta  */
    @Transactional
    public void confirmarCuenta(String codConfirmacion) throws ErrorUsuario {
        Optional<Usuario> thisUser = usuarioRepositorio.findByConfirmationId(codConfirmacion);

        if (thisUser.isPresent()) {
            Usuario usuario = thisUser.get();
            usuario.setConfirmado(true);
            usuarioRepositorio.save(usuario);
        } else {
            logger.error(ErrorUsuario.COD_CONFIRM);
            throw new ErrorUsuario(ErrorUsuario.COD_CONFIRM);
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
    
    /* Metodo para setear el atributo admin como verdadero */
    
    @Transactional
    public void altaAdmin(String email) throws ErrorUsuario{
    	Optional<Usuario> thisUser = usuarioRepositorio.findByEmail(email);
    	
    	if(thisUser.isPresent()) {
    		Usuario usuario = thisUser.get();
    		
    		if(usuario.getAdmin() == true) {
    			throw new ErrorUsuario(ErrorUsuario.ALREADY_ADMIN);
    		}
    		
    		if(usuario.getConfirmado() == false) {
    			throw new ErrorUsuario(ErrorUsuario.NO_ACTIVO);
    		}
    		
    		usuario.setAdmin(true);
    		usuarioRepositorio.save(usuario);
    	}else {
    		throw new ErrorUsuario(ErrorUsuario.NO_EXISTE);
    	}
    }

    /*  Metodo de login del usuario, si el usuario no existe o esta dado de baja va retornar null */

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> thisUser = usuarioRepositorio.findByEmail(email);

        if (thisUser.isPresent()) {

            Usuario usuario = thisUser.get();

            if (usuario.getAlta() && usuario.getConfirmado()) {
                List<GrantedAuthority> permisos = new ArrayList<>();

                /*  Definicion de los permisos en base al tipo de usuario */
                GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO");
                GrantedAuthority p2 = new SimpleGrantedAuthority("ROLE_APUESTA");
                GrantedAuthority p3 = new SimpleGrantedAuthority("ROLE_ADMIN");
                
                if(usuario.getAdmin()){
                    permisos.add(p3);
                }
                
                permisos.add(p1);
                permisos.add(p2);

                ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpSession session = attr.getRequest().getSession(true);
                session.setAttribute("sesionUsuario", usuario);

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
    
    /*  Metodos de listado  */
    
    public long contarTodos(){
        return usuarioRepositorio.count();
    }

    public Optional<Usuario> buscarPorId(String id){
        return usuarioRepositorio.findById(id);
    }
    
    public Optional<Usuario> buscarPorEmail(String email){
    	return usuarioRepositorio.findByEmail(email);
    }
    
    public List<Usuario> listarAdmins(){
    	Optional<List<Usuario>> thisList = usuarioRepositorio.findByAdminRole();
    	
    	if(thisList.isPresent()) {
    		return thisList.get();
    	}else {
    		return Collections.emptyList();
    	}
    }
}
