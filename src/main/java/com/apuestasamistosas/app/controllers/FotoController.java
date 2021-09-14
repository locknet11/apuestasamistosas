
package com.apuestasamistosas.app.controllers;

import com.apuestasamistosas.app.entities.Equipos;
import com.apuestasamistosas.app.entities.Premio;
import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.errors.ErrorEquipos;
import com.apuestasamistosas.app.errors.ErrorUsuario;
import com.apuestasamistosas.app.services.EquiposServicio;
import com.apuestasamistosas.app.services.PremioServicio;
import com.apuestasamistosas.app.services.UsuarioServicio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/photos")
public class FotoController {

    @Autowired
    private PremioServicio premioServicio;
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private EquiposServicio equipoServicio;
    
    /*  SECCION PREMIOS  */

    @GetMapping("/rewards")
    public ResponseEntity<byte[]> rewardPhoto(@RequestParam String id) {
        Optional<Premio> premio = premioServicio.buscarPorId(id);
        try {
            byte[] fotoPremio = premio.get().getFoto().getContenido();   
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(fotoPremio, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /*  SECCION USUARIO  */
    
    @GetMapping("/users")
    public ResponseEntity<byte[]> userPhoto(@RequestParam String id) throws ErrorUsuario {
        Optional<Usuario> usuario = usuarioServicio.buscarPorId(id);

        try {
            if (!usuario.isPresent()) {
                throw new ErrorUsuario(ErrorUsuario.NO_EXISTE);
            } else {
                if (usuario.get().getFoto() == null) {
                    throw new ErrorUsuario(ErrorUsuario.NO_FOTO);
                }
            }
            byte[] fotoUsuario = usuario.get().getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(fotoUsuario, headers, HttpStatus.OK);
        } catch (ErrorUsuario e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    
    /* SECCION EQUIPOS	*/
    
    @GetMapping("/teams")
    public ResponseEntity<byte[]> teamPhoto(@RequestParam String id) throws ErrorEquipos{
    	Optional<Equipos> equipo = equipoServicio.buscarPorId(id);
    	
    	try {
    		if(!equipo.isPresent()) {
    			throw new ErrorEquipos(ErrorEquipos.NO_EXISTS);
    		}
    		byte[] fotoEquipo = equipo.get().getFoto().getContenido();
    		HttpHeaders headers = new HttpHeaders();
    		headers.setContentType(MediaType.IMAGE_JPEG);
    		return new ResponseEntity<>(fotoEquipo, headers, HttpStatus.OK);
    	}catch(ErrorEquipos e) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    }
    
    
    
    
    
    
    
    
    
}
