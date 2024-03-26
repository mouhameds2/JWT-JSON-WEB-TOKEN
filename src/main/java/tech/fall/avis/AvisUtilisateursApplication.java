package tech.fall.avis;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.fall.avis.entite.Role;
import tech.fall.avis.entite.Utilisateur;
import tech.fall.avis.enume.TypeDeRole;
import tech.fall.avis.repository.UtilisateurRepository;

import static tech.fall.avis.enume.TypeDeRole.ADMINISTRATEUR;

//Annotation pour pour programer des taches ici on programme pour supprimer nos tokens unitils
@AllArgsConstructor
@EnableScheduling
@SpringBootApplication
public class AvisUtilisateursApplication implements CommandLineRunner {
	UtilisateurRepository utilisateurRepository;
	PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(AvisUtilisateursApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!this.utilisateurRepository.findByEmail("mouhameds2@hotmail.fr").isPresent()){


		Utilisateur admin = Utilisateur.builder()

				.actif(true)
				.nom("Admin")
				.mdp(passwordEncoder.encode("password10"))
				.role(
						Role.builder()
								.libelle(ADMINISTRATEUR)
								.build()
				)
				.email("mouhameds2@hotmail.fr")
				.build();
		this.utilisateurRepository.save(admin);
		}



		if (!this.utilisateurRepository.findByEmail("metatchek@gmail.com").isPresent()) {
			Utilisateur manager = Utilisateur.builder()
					.actif(true)
					.nom("Manager")
					.mdp(passwordEncoder.encode("Manager"))
					.role(
							Role.builder()
									.libelle(TypeDeRole.MANAGER)
									.build()
					)
					.email("metatchek@gmail.com")
					.build();
			this.utilisateurRepository.save(manager);


		}
	}
}
