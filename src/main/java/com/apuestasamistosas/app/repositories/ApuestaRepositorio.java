package com.apuestasamistosas.app.repositories;

import com.apuestasamistosas.app.entities.Apuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface ApuestaRepositorio extends JpaRepository<Apuesta, String> {

	@Query(value = "SELECT * FROM apuestas a WHERE (a.usuarioA_id = :id OR a.usuarioB_id = :id) AND a.estado = :state", nativeQuery=true)
	public Optional<List<Apuesta>> findByUserAndState(@Param("id") String id, @Param("state") String state);

	@Query("SELECT a FROM Apuesta a WHERE evento_id = :id AND estado = 'CONFIRMADA'")
	public Optional<List<Apuesta>> findByEvent(@Param("id") String id);
}
