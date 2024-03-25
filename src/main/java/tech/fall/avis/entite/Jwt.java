package tech.fall.avis.entite;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jwt")
public class Jwt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String valeur;
    private boolean desactiveted;
    private boolean expire;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE})
    @JoinColumn(name = "utilisateur_id")
    public Utilisateur utilisateur;

}
