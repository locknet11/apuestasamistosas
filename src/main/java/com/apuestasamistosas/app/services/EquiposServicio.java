
package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Equipos;
import com.apuestasamistosas.app.entities.Foto;
import com.apuestasamistosas.app.errors.ErrorEquipos;
import com.apuestasamistosas.app.repositories.EquiposRepositorio;
import com.apuestasamistosas.app.validations.EquiposValidacion;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EquiposServicio {
    
    Logger logger = LoggerFactory.getLogger(EquiposServicio.class);
    
    @Autowired
    private EquiposValidacion ev;
    
    @Autowired
    private EquiposRepositorio equipoRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;
    
    /*  Metodo de registro de equipos */
    
    @Transactional
    public void registroEquipo(String nombre, String deporte, MultipartFile archivo) throws ErrorEquipos, Exception{
        
        ev.validarDatos(nombre, deporte, archivo);
        
        Equipos equipo = new Equipos();
        Foto foto = fotoServicio.guardar(archivo);
        equipo.setNombre(nombre);
        equipo.setDeporte(deporte);
        equipo.setAlta(true);
        equipo.setFoto(foto);
        
        equipoRepositorio.save(equipo);
        
    }
    
    /*  Metodos de listado  */
    
    public long contarTodos(){
        return equipoRepositorio.count();
    }
    
    public List<Equipos> listarObjetos(){
        return equipoRepositorio.findAll();
    }
    
    public List<String> listarNombres(){
        return equipoRepositorio.listarNombresEquipos();
    }
    
    public Optional<Equipos> buscarPorId(String id){
    	return equipoRepositorio.findById(id);
    }
    
}
