package tech.chillo.avis.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.chillo.avis.dto.AuthentificationDTO;
import tech.chillo.avis.dto.UtilisateurDTO;
import tech.chillo.avis.mapper.UtilisateurDTOMapper;
import tech.chillo.avis.repository.UtilisateurRepository;
import tech.chillo.avis.TypeDeRole;
import tech.chillo.avis.entite.Role;
import tech.chillo.avis.entite.Utilisateur;
import tech.chillo.avis.entite.Validation;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.hibernate.Hibernate.map;

@AllArgsConstructor

@Service
public class UtilisateurService implements UserDetailsService {

    private UtilisateurRepository utilisateurRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private ValidationService validationService;
    private UtilisateurDTOMapper utilisateurDTOMapper;


    public void inscription(Utilisateur utilisateur) {

        if(!utilisateur.getEmail().contains("@")) {
            throw  new RuntimeException("Votre mail invalide");
        }
        if(!utilisateur.getEmail().contains(".")) {
            throw  new RuntimeException("Votre mail invalide");
        }

        Optional<Utilisateur> utilisateurOptional = this.utilisateurRepository.findByEmail(utilisateur.getEmail());
        if(utilisateurOptional.isPresent()) {
            throw  new RuntimeException("** le client " + utilisateur.getEmail() +  " a déja un compte merci de vous conecter " +
                    "ou de réinitialiser votre mot de passe **");
        }
        String mdpCrypte = this.passwordEncoder.encode(utilisateur.getMdp());
        utilisateur.setMdp(mdpCrypte);

        Role roleUtilisateur = new Role();
        roleUtilisateur.setLibelle(TypeDeRole.UTILISATEUR);
        utilisateur.setRole(roleUtilisateur);

        utilisateur = this.utilisateurRepository.save(utilisateur);
        this.validationService.enregistrer(utilisateur);
    }

    public void activation(Map<String, String> activation) {
        Validation validation = this.validationService.lireEnFonctionDuCode(activation.get("code"));
        if(Instant.now().isAfter(validation.getExpiration())){
            throw  new RuntimeException("Votre code a expiré");
        }
        Utilisateur utilisateurActiver = this.utilisateurRepository.findById(validation.getUtilisateur().getId()).orElseThrow(() ->
                new RuntimeException("Utilisateur  inconnu"));
        utilisateurActiver.setActif(true);
        this.utilisateurRepository.save(utilisateurActiver);
    }


    //loadUserByUsername part chercher un user dans la base de données en fonction du mail passé et puis comparer les mot de passe
    @Override
    public Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.utilisateurRepository
                .findByEmail(username)
                .orElseThrow(() -> new  UsernameNotFoundException("Aucun utilisateur ne corespond à cet identifiant"));
    }


    public Stream<UtilisateurDTO> getUtilisateurById(int id) {

        Stream<UtilisateurDTO> optionalUtilisateurDTO = this.utilisateurRepository.findById(id)
                .stream().map(utilisateurDTOMapper);
        if (optionalUtilisateurDTO == null) {
            throw new EntityNotFoundException("le client " + id + " n'existe pas");
        }
        return optionalUtilisateurDTO;
    }
    private UtilisateurDTO convertToUtilisateurDTO(Utilisateur utilisateur){

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO(utilisateur.getId(), utilisateur.getEmail(), utilisateur.getNom());
        return utilisateurDTO ;

    }

    public Stream<UtilisateurDTO>  getUtilisateur() {

        return  StreamSupport.stream(this.utilisateurRepository.findAll().spliterator(),false).
                map(utilisateur -> convertToUtilisateurDTO(utilisateur));
              
    }
}
