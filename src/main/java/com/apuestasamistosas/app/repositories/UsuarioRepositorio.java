package com.apuestasamistosas.app.repositories;

import com.apuestasamistosas.app.entities.Usuario;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Optional<Usuario> findByEmail(@Param("email") String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.codConfirmacion = :codConfirmacion")
    public Optional<Usuario> findByConfirmationId(@Param("codConfirmacion") String codConfirmacion);

    @Query("SELECT u FROM Usuario u WHERE u.admin = true")
    public Optional<List<Usuario>> findByAdminRole();
    
    @Query(value = "SELECT * FROM usuarios u ORDER BY u.ganados DESC LIMIT 10", nativeQuery=true)
    public Optional<List<Usuario>> listByWin();
}
