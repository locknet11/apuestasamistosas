
package com.apuestasamistosas.app.validations;

import com.apuestasamistosas.app.entities.Proveedores;
import com.apuestasamistosas.app.errors.ErrorPremio;
import com.apuestasamistosas.app.errors.ErrorProveedores;
import com.apuestasamistosas.app.repositories.ProveedoresRepositorio;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PremioValidacion {
          
    Logger logger = LoggerFactory.getLogger(PremioValidacion.class);

    @Autowired
    private ProveedoresRepositorio proveedorRepositorio;
    
    /*  La validacion que se realiza es que el Proveedor exista, que el nombre no este vacio,
        y que el precio no sea negativo. De tal forma que el usuario no
        puede ingresar proveedores inexistentes.
    */
    
    public void validarDatos(String nombre, String nombreProveedor, Double precio, MultipartFile archivo) throws ErrorPremio{
        
        
        /*  Verificamos que el nombre y el nombre del proveedor no este vacio */
        
        if(nombre == null || nombre.isEmpty()){
            logger.error(ErrorPremio.NO_NOMBRE);
            throw new ErrorPremio(ErrorPremio.NO_NOMBRE);
        }

        if (nombreProveedor == null || nombreProveedor.isEmpty()) {
            logger.error(ErrorPremio.NO_NOMBRE);
            throw new ErrorPremio(ErrorPremio.NO_NOMBRE);
        } else {
            /*  Verificamos que el proveedor exista. */
            
            Optional<Proveedores> thisProveedor = proveedorRepositorio.findByName(nombreProveedor);

            if (!thisProveedor.isPresent()) {
                logger.error(ErrorProveedores.NO_PROV);
                throw new ErrorPremio(ErrorProveedores.NO_PROV);
            }
        }
        
        if(archivo == null || archivo.isEmpty()){
            logger.error(ErrorPremio.NULL_FOTO);
            throw new ErrorPremio(ErrorPremio.NULL_FOTO);
        }
        
        /* Verificamos que el precio no sea negativo  */
        
        if (precio == null || precio <= 0){
            logger.error(ErrorPremio.PRECIO_NEG);
            throw new ErrorPremio(ErrorPremio.PRECIO_NEG);
        }
    }

    

}
