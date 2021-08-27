
package com.apuestasamistosas.app.repositories;
import com.apuestasamistosas.app.entities.Transaccion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionRepositorio extends JpaRepository<Transaccion, String> {
    
    @Query("SELECT p FROM Transaccion p WHERE p.saldo = :saldo")
    public Optional<Transaccion> findByName(@Param("saldo") String nombre);
}
