package tech.fall.avis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//Annotation pour pour programer des taches ici on programme pour supprimer nos tokens unitils
@EnableScheduling
@SpringBootApplication
public class AvisUtilisateursApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvisUtilisateursApplication.class, args);
	}

}
