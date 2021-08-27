
package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Proveedores;
import com.apuestasamistosas.app.errors.ErrorProveedores;
import com.apuestasamistosas.app.repositories.ProveedoresRepositorio;
import com.apuestasamistosas.app.validations.ProveedoresValidacion;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProveedoresServicio {
    
    @Autowired
    private ProveedoresRepositorio proveedorRepositorio;
    
    Logger logger = LoggerFactory.getLogger(ProveedoresServicio.class);
    
    @Autowired
    ProveedoresValidacion pv = new ProveedoresValidacion();
    
    /*  Metodo de registro de proveedor */
    
    @Transactional
    public void registroProveedor(String nombre, String provincia,
            String ciudad, String calle, String codigoPostal, String telefono,
            String responsable) throws ErrorProveedores{
        
        pv.validarDatos(nombre, telefono, responsable);
        
        Proveedores proveedor = new Proveedores();
        
        proveedor.setNombre(nombre);
        proveedor.setAlta(true);
        proveedor.setProvincia(provincia);
        proveedor.setCiudad(ciudad);
        proveedor.setCalle(calle);
        proveedor.setCodigoPostal(codigoPostal);
        proveedor.setTelefono(telefono);
        proveedor.setResponsable(responsable);
        
        proveedorRepositorio.save(proveedor);
        
    }
    
    /*  Metodo de modificacion de datos del proveedor, no se puede cambiar el nombre  */
    
    @Transactional
    public void modificarProveedor(String id, String provincia,
            String ciudad, String calle, String codigoPostal, String telefono,
            String responsable ) throws ErrorProveedores{
        
        pv.validarDatosModificar(telefono, responsable);
        
        Optional<Proveedores> thisProveedor = proveedorRepositorio.findById(id);
        
        if (thisProveedor.isPresent()){
            
            Proveedores proveedor = thisProveedor.get();
            proveedor.setAlta(true);
            proveedor.setProvincia(provincia);
            proveedor.setCiudad(ciudad);
            proveedor.setCalle(calle);
            proveedor.setCodigoPostal(codigoPostal);
            proveedor.setTelefono(telefono);
            proveedor.setResponsable(responsable);
            
            proveedorRepositorio.save(proveedor);
        }else{
            logger.error(ErrorProveedores.NO_PROV);
            throw new ErrorProveedores(ErrorProveedores.NO_PROV);
        }
    }
    
    /*  Metodo para dar de baja un proveedor */
    
    @Transactional
    public void bajaProveedor(String id) throws ErrorProveedores {
        Optional<Proveedores> thisProveedor = proveedorRepositorio.findById(id);
        
        if (thisProveedor.isPresent()) {
            
            Proveedores proveedor = thisProveedor.get();
            proveedor.setAlta(false);
            
            proveedorRepositorio.save(proveedor);

        }else {
            logger.error(ErrorProveedores.NO_PROV);
            throw new ErrorProveedores(ErrorProveedores.NO_PROV);
        }

    }

    /*  Metodo para dar de alta un proveedor */
    
        @Transactional
    public void altaProveedor(String id) throws ErrorProveedores {
        Optional<Proveedores> thisProveedor = proveedorRepositorio.findById(id);
        
        if (thisProveedor.isPresent()) {
            
            Proveedores proveedor = thisProveedor.get();
            proveedor.setAlta(true);
            
            proveedorRepositorio.save(proveedor);

        }else {
            logger.error(ErrorProveedores.NO_PROV);
            throw new ErrorProveedores(ErrorProveedores.NO_PROV);
        }

    }
    
    /*  Listar informacion  */
    
    public List<String> listarNombres(){
        return proveedorRepositorio.listarNombres();
    }
    
    public long contarTodos(){
        return proveedorRepositorio.count();
    }


}
