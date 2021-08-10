package com.apuestasamistosas.app.repositories;

import com.apuestasamistosas.app.entities.Apuesta;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ApuestaRepositorio extends JpaRepository<Apuesta, String> {

}
