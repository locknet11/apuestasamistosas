package com.apuestasamistosas.app.repositories;

import com.apuestasamistosas.app.entities.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FotoRepositorio extends JpaRepository<Foto, String> {
    
    
}