
package com.apuestasamistosas.app.validations;

import com.apuestasamistosas.app.entities.Equipos;
import com.apuestasamistosas.app.errors.ErrorEquipos;
import com.apuestasamistosas.app.repositories.EquiposRepositorio;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class EquiposValidacion {
    
    Logger logger = LoggerFactory.getLogger(EquiposValidacion.class);
    
    @Autowired
    private EquiposRepositorio equipoRepositorio;
    
    /*  Validacion de datos */
    
    public void validarDatos(String nombre, String deporte, MultipartFile archivo) throws ErrorEquipos, Exception {
        
        Optional<Equipos> equipo = equipoRepositorio.findByName(nombre);
        
        if(nombre == null || nombre.isEmpty()){
            logger.error(ErrorEquipos.NULL_EQP);
            throw new ErrorEquipos(ErrorEquipos.NULL_EQP);
        }
        
        if(deporte == null || deporte.isEmpty()){
            logger.error(ErrorEquipos.NULL_SPORT);
            throw new ErrorEquipos(ErrorEquipos.NULL_SPORT);
        }
        
        if(archivo == null || archivo.isEmpty()){
            logger.error(ErrorEquipos.NULL_FOTO);
            throw new ErrorEquipos(ErrorEquipos.NULL_FOTO);
        }
        
        if(equipo.isPresent()){
            logger.error(ErrorEquipos.EQP_EXISTS);
            throw new ErrorEquipos(ErrorEquipos.EQP_EXISTS);
        }
        
    }

}
