package com.apuestasamistosas.app.repositories;

import com.apuestasamistosas.app.entities.Eventos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventosRepositorio extends JpaRepository<Eventos, String> {

}
