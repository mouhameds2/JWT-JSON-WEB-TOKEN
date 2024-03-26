package tech.fall.avis.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tech.fall.avis.repository.ValidationRepository;
import tech.fall.avis.entite.Utilisateur;
import tech.fall.avis.entite.Validation;

import java.time.Instant;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;

@Transactional
@AllArgsConstructor
@Service
public class ValidationService {

    private ValidationRepository validationRepository;
    private NotificationService notificationService;

    public void enregistrer(Utilisateur utilisateur) {
        Validation validation = new Validation();
        validation.setUtilisateur(utilisateur);
        Instant creation = Instant.now();
        validation.setCreation(creation);
        Instant expiration = creation.plus(10, MINUTES);
        validation.setExpiration(expiration);
        Random random = new Random();
        int randomInteger = random.nextInt(999999);
        String code = String.format("%06d", randomInteger);

        validation.setCode(code);
        this.validationRepository.save(validation);
        this.notificationService.envoyer(validation);
    }

    public Validation lireEnFonctionDuCode(String code) {
        return this.validationRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Votre code est invalide"));
    }

    // Suppression des jetons automatique 1 minute après leur expiration
    @Scheduled(cron = "0 */1 * * * * ")
    public  void supprimerCodeExpire() {
        this.validationRepository.deleteAllByExpirationBefore(Instant.now());
    }
}
