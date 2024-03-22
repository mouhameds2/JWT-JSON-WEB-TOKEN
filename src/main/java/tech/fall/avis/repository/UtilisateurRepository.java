package tech.fall.avis.repository;

import org.springframework.data.repository.CrudRepository;
import tech.fall.avis.entite.Utilisateur;

import java.util.Optional;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByEmail(String email);
}
