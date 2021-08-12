
package com.apuestasamistosas.app.validations;

import com.apuestasamistosas.app.entities.Proveedores;
import com.apuestasamistosas.app.errors.ErrorProveedores;
import com.apuestasamistosas.app.repositories.ProveedoresRepositorio;
import com.apuestasamistosas.app.services.ProveedoresServicio;
import com.apuestasamistosas.app.services.UsuarioServicio;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class ProveedoresValidacion {
    
      Logger logger = LoggerFactory.getLogger(ProveedoresServicio.class);
      
      @Autowired
      private ProveedoresRepositorio proveedorRepositorio;

    
    public void validarDatos(String nombre, String telefono, String responsable) throws ErrorProveedores{
        
        /*  Validamos que el nombre del proveedor y del responsable no sea una cadena vacia */
        
        if (nombre == null || nombre.isEmpty()){
            logger.error(ErrorProveedores.NO_NOMBRE);
            throw new ErrorProveedores(ErrorProveedores.NO_NOMBRE);
        } else {
            
            Optional<Proveedores> thisProveedor = proveedorRepositorio.findByName(nombre);

            /*  Valido que el proveedor no este previamente registrado */
            
            if (thisProveedor.isPresent()) {
                logger.error(ErrorProveedores.PROV_EXISTS);
                throw new ErrorProveedores(ErrorProveedores.PROV_EXISTS);
            }
        }
        
        if (responsable == null || responsable.isEmpty()) {
            logger.error(ErrorProveedores.NO_RESP);
            throw new ErrorProveedores(ErrorProveedores.NO_RESP);
        }

        

        /* Validamos que el telefono solo tenga numeros (maximo 13 digitos) y no este vacio */
        
        if (telefono == null || telefono.isEmpty()) {
            logger.error(ErrorProveedores.NO_TEL);
            throw new ErrorProveedores(ErrorProveedores.NO_TEL);
        } else {
            try {
                Long num = Long.parseLong(telefono);
                if (num > 13) {
                    logger.error(ErrorProveedores.DIGIT_TEL);
                    throw new ErrorProveedores(ErrorProveedores.DIGIT_TEL);
                }
            } catch (NumberFormatException e) {
                logger.error(ErrorProveedores.DIGIT_TEL);
                throw new ErrorProveedores(ErrorProveedores.DIGIT_TEL);
            }
        }
    }
    
    public void validarDatosModificar(String telefono, String responsable) throws ErrorProveedores{
        
        /* validamos que el responsable no este vacio */ 
        
        if (responsable == null || responsable.isEmpty()) {
            logger.error(ErrorProveedores.NO_RESP);
            throw new ErrorProveedores(ErrorProveedores.NO_RESP);
        }

        

        /* Validamos que el telefono solo tenga numeros (maximo 13 digitos) y no este vacio */
        
        if (telefono == null || telefono.isEmpty()) {
            logger.error(ErrorProveedores.NO_TEL);
            throw new ErrorProveedores(ErrorProveedores.NO_TEL);
        } else {
            try {
                Long num = Long.parseLong(telefono);
                if (num > 13) {
                    logger.error(ErrorProveedores.DIGIT_TEL);
                    throw new ErrorProveedores(ErrorProveedores.DIGIT_TEL);
                }
            } catch (NumberFormatException e) {
                logger.error(ErrorProveedores.DIGIT_TEL);
                throw new ErrorProveedores(ErrorProveedores.DIGIT_TEL);
            }
        }
    }

}
