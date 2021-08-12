package com.apuestasamistosas.app.repositories;

import com.apuestasamistosas.app.entities.Premio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PremioRepositorio extends JpaRepository<Premio, String> {

}
