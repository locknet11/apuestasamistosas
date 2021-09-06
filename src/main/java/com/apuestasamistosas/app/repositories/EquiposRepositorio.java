package com.apuestasamistosas.app.repositories;

import com.apuestasamistosas.app.entities.Equipos;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EquiposRepositorio extends JpaRepository<Equipos, String>{
    
    @Query("SELECT e FROM Equipos e WHERE e.nombre = :nombre")
    public Optional<Equipos> findByName(@Param("nombre") String nombre);
    
    @Query("SELECT e.nombre FROM Equipos e")
    public List<String> listarNombresEquipos();
    
}

