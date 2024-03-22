package tech.chillo.avis.dto;

import tech.chillo.avis.entite.Utilisateur;

import java.util.function.Function;

public record AuthentificationDTO(String username, String password)  {
}
