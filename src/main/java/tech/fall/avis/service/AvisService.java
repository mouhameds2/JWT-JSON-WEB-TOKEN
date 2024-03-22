package tech.fall.avis.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tech.fall.avis.entite.Avis;
import tech.fall.avis.entite.Utilisateur;
import tech.fall.avis.repository.AvisRepository;

@AllArgsConstructor
@Service
public class AvisService {

    private final AvisRepository avisRepository;

    public void creer(Avis avis){
       Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       avis.setUtilisateur(utilisateur);
        this.avisRepository.save(avis);
    }
}
