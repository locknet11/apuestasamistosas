
package com.apuestasamistosas.app.validations;

import com.apuestasamistosas.app.entities.Proveedores;
import com.apuestasamistosas.app.errors.ErrorPremio;
import com.apuestasamistosas.app.errors.ErrorProveedores;
import com.apuestasamistosas.app.repositories.ProveedoresRepositorio;
import com.apuestasamistosas.app.services.ProveedoresServicio;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class PremioValidacion {
          
    Logger logger = LoggerFactory.getLogger(ProveedoresServicio.class);

    @Autowired
    private ProveedoresRepositorio proveedorRepositorio;
    
    /*  La validacion que se realiza es que el Proveedor exista, que el nombre no este vacio,
        y que el precio no sea negativo. De tal forma que el usuario no
        puede ingresar proveedores inexistentes.
    */
    
    public void validarDatos(String nombre, String nombreProveedor, Double precio) throws ErrorPremio{
        
        Optional<Proveedores> thisProveedor = proveedorRepositorio.findByName(nombreProveedor);
        
        /*  Verificamos que el nombre no este vacio */
        
        if(nombre.isEmpty()){
            logger.error(ErrorPremio.NO_NOMBRE);
            throw new ErrorPremio(ErrorPremio.NO_NOMBRE);
        }
        
        /*  Verificamos que el proveedor exista. */
        
        if (!thisProveedor.isPresent()){
            logger.error(ErrorProveedores.NO_PROV);
            throw new ErrorPremio(ErrorProveedores.NO_PROV);
        }
        
        /* Verificamos que el precio no sea negativo  */
        
        if (precio <= 0){
            logger.error(ErrorPremio.PRECIO_NEG);
            throw new ErrorPremio(ErrorPremio.PRECIO_NEG);
        }
    }

    

}
