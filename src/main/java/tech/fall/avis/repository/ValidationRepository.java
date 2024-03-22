package tech.fall.avis.repository;

import org.springframework.data.repository.CrudRepository;
import tech.fall.avis.entite.Validation;

import java.util.Optional;

public interface ValidationRepository extends CrudRepository<Validation, Integer> {

    Optional<Validation> findByCode(String code);
}