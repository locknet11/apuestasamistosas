package com.apuestasamistosas.app.repositories;

import com.apuestasamistosas.app.entities.Premio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PremioRepositorio extends JpaRepository<Premio, String> {

	@Query("SELECT p FROM Premio p")
	public Optional<List<Premio>> listAllRewards();
}
