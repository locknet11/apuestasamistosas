package com.apuestasamistosas.app.repositories;

import com.apuestasamistosas.app.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{

}
