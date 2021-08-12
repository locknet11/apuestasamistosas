package com.apuestasamistosas.app.repositories;

import com.apuestasamistosas.app.entities.direcciones.Direcciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinciaRepositorio extends JpaRepository<Direcciones, Long>{


}
