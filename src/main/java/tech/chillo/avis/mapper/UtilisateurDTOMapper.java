package tech.chillo.avis.mapper;

import org.springframework.stereotype.Component;


import tech.chillo.avis.dto.UtilisateurDTO;
import tech.chillo.avis.entite.Utilisateur;

import java.util.function.Function;

@Component
public class UtilisateurDTOMapper implements Function<Utilisateur, UtilisateurDTO> {
    @Override
    public UtilisateurDTO apply(Utilisateur utilisateur) {
        return  new UtilisateurDTO(utilisateur.getId(),utilisateur.getEmail(), utilisateur.getNom());


   }
}
