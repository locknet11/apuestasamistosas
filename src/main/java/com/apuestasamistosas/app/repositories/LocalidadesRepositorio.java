package com.apuestasamistosas.app.repositories;

import com.apuestasamistosas.app.entities.direcciones.localidades.LocalidadesDatos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalidadesRepositorio extends JpaRepository<LocalidadesDatos, Long>{


}
