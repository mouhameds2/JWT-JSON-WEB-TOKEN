package tech.fall.avis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.fall.avis.entite.Avis;

public interface AvisRepository extends JpaRepository<Avis, Integer> {
}
