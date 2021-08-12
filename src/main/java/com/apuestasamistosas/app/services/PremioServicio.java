package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Premio;
import com.apuestasamistosas.app.entities.Proveedores;
import com.apuestasamistosas.app.errors.ErrorPremio;
import com.apuestasamistosas.app.repositories.PremioRepositorio;
import com.apuestasamistosas.app.repositories.ProveedoresRepositorio;
import com.apuestasamistosas.app.validations.PremioValidacion;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PremioServicio {

    Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);
    
    private PremioValidacion pv = new PremioValidacion();
    
    @Autowired
    private PremioRepositorio premioRepositorio;
    
    @Autowired
    private ProveedoresRepositorio proveedorRepositorio;
    
    /*  Metodo de registro de premios  */
    
    @Transactional
    public void registroPremio(String nombre, Double precio, String nombreProveedor) throws ErrorPremio{
        
        pv.validarDatos(nombre, nombreProveedor, precio);
        
        Optional<Proveedores> thisProveedor = proveedorRepositorio.findByName(nombreProveedor);
        Proveedores proveedor = thisProveedor.get();
        Premio premio = new Premio();
        premio.setNombre(nombre);
        premio.setPrecio(precio);
        premio.setAlta(true);
        premio.setProveedor(proveedor);
        premio.setRanking(0);
        
        premioRepositorio.save(premio);
    }
    
    /* Metodo de modificacion de premio*/
    
    @Transactional
    public void modificarPremio(String id, String nombre, Double precio, String nombreProveedor) throws ErrorPremio{
         
        pv.validarDatos(nombre, nombreProveedor, precio);
        Optional<Premio> thisPremio = premioRepositorio.findById(id);
        Optional<Proveedores> thisProveedor = proveedorRepositorio.findByName(nombreProveedor);
        
        if(thisPremio.isPresent()){
            Premio premio = thisPremio.get();
            Proveedores proveedor = thisProveedor.get();
            premio.setNombre(nombre);
            premio.setPrecio(precio);
            premio.setAlta(true);
            premio.setProveedor(proveedor);
            
            premioRepositorio.save(premio);
        }else{
            logger.error(ErrorPremio.NO_EXISTS);
            throw new ErrorPremio(ErrorPremio.NO_EXISTS);
        } 
    }
    
    /* Metodo de baja de un premio */
    
    @Transactional
    public void bajaPremio(String id){
         Optional<Premio> thisPremio = premioRepositorio.findById(id);
         if(thisPremio.isPresent()){
             Premio premio = thisPremio.get();
             premio.setAlta(false);
             premioRepositorio.save(premio);
         }
    }
    
    /* Metodo de alta de un premio */
    
    @Transactional
    public void altaPremio(String id){
         Optional<Premio> thisPremio = premioRepositorio.findById(id);
         if(thisPremio.isPresent()){
             Premio premio = thisPremio.get();
             premio.setAlta(true);
             premioRepositorio.save(premio);
         }
    }
    

}
