package com.apuestasamistosas.app.repositories;

import com.apuestasamistosas.app.entities.Proveedores;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProveedoresRepositorio extends JpaRepository<Proveedores, String> {

    @Query("SELECT p FROM Proveedores p WHERE p.nombre = :nombre")
    public Optional<Proveedores> findByName(@Param("nombre") String nombre);
}
