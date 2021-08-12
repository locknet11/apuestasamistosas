package com.apuestasamistosas.app.repositories;

import com.apuestasamistosas.app.entities.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinciaRepositorio extends JpaRepository<Provincia, String>{

}
