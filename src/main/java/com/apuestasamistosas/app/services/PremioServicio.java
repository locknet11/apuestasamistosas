package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Foto;
import com.apuestasamistosas.app.entities.Premio;
import com.apuestasamistosas.app.entities.Proveedores;
import com.apuestasamistosas.app.errors.ErrorPremio;
import com.apuestasamistosas.app.repositories.PremioRepositorio;
import com.apuestasamistosas.app.repositories.ProveedoresRepositorio;
import com.apuestasamistosas.app.validations.PremioValidacion;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PremioServicio {
    
    Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);
    
    @Autowired
    private PremioValidacion pv = new PremioValidacion();
    
    @Autowired
    private PremioRepositorio premioRepositorio;
    
    @Autowired
    private ProveedoresRepositorio proveedorRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;

    
    /*  Metodo de registro de premios  */
    
    @Transactional
    public void registroPremio(String nombre, Double precio, String nombreProveedor, MultipartFile archivo) throws ErrorPremio, Exception{
        
        pv.validarDatos(nombre, nombreProveedor, precio, archivo);
        
        Optional<Proveedores> thisProveedor = proveedorRepositorio.findByName(nombreProveedor);
        Proveedores proveedor = thisProveedor.get();
        Foto foto = fotoServicio.guardar(archivo);
        Premio premio = new Premio();
        premio.setNombre(nombre);
        premio.setPrecio(precio);
        premio.setAlta(true);
        premio.setProveedor(proveedor);
        premio.setRanking(0);
        premio.setFoto(foto);
        
        premioRepositorio.save(premio);
    }
    
    /* Metodo de modificacion de premio*/
    
    @Transactional
    public void modificarPremio(String id, String nombre, Double precio, String nombreProveedor, MultipartFile archivo) throws ErrorPremio, Exception{
         
        pv.validarDatos(nombre, nombreProveedor, precio, archivo);
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
    
    
    /*  Metodos de listado */
    
    public long contarTodos(){
        return premioRepositorio.count();
    }
    
    public List<Premio> listarTodos(){
        return premioRepositorio.findAll();
    }
    
    public Optional<Premio> buscarPorId(String id){
        return premioRepositorio.findById(id);
    }

}
