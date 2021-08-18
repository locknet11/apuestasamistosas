package com.apuestasamistosas.app.repositories;

import com.apuestasamistosas.app.entities.Apuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApuestaRepositorio extends JpaRepository<Apuesta, String> {

}
