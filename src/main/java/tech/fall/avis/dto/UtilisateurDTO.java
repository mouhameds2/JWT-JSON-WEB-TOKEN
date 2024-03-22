package tech.fall.avis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;



public record UtilisateurDTO(
        int id,
        String email,
        String nom
) {

}
