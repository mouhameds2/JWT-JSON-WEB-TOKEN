package tech.fall.avis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.fall.avis.entite.Jwt;

import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends JpaRepository<Jwt, Integer> {
    Optional<Jwt> findByValeur(String valeur);
    Optional<Jwt> findByValeurAndDesactivetedAndExpire(String valeur, boolean desactiveted, boolean expire);


    @Query("FROM Jwt j WHERE j.utilisateur.email= :email AND j.desactiveted = :desactiveted AND j.expire = :expire")
    Optional<Jwt> findUtilisateurValidToken(String email, boolean desactiveted, boolean expire);





    @Query("FROM Jwt j WHERE j.utilisateur.email= :email")
    Stream<Jwt> findAllTokensUtilisateur(String email);




    void deleteAllByExpireAndDesactiveted(boolean expire, boolean desactive);

}
