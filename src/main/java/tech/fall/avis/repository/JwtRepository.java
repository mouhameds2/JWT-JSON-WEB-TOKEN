package tech.fall.avis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.fall.avis.entite.Jwt;

import java.util.Optional;

public interface JwtRepository extends JpaRepository<Jwt, Integer> {
    Optional<Jwt> findByValeur(String valeur);
}
