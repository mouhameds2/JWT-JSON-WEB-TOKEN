package tech.fall.avis.controleur;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.fall.avis.dto.AuthentificationDTO;
import tech.fall.avis.dto.UtilisateurDTO;
import tech.fall.avis.entite.Utilisateur;
import tech.fall.avis.securite.JwtService;
import tech.fall.avis.service.UtilisateurService;

import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "utilisateur")
public class UtilisateurControleur {


    private AuthenticationManager authenticationManager;

    private UtilisateurService utilisateurService;

    private JwtService jwtService;

    @PostMapping(path = "inscription",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void inscription(@RequestBody Utilisateur utilisateur) {
        log.info("Inscription");
        this.utilisateurService.inscription(utilisateur);
    }

    @PostMapping(path = "activation",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void activation(@RequestBody Map<String, String> activation) {
        this.utilisateurService.activation(activation);
    }

    @PostMapping(path = "connexion",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> connexion(@RequestBody AuthentificationDTO authentificationDTO) {
       final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentificationDTO.username(), authentificationDTO.password())
        );
       log.info("resultat {}", authenticate.isAuthenticated()
       );


        if(authenticate.isAuthenticated()) {
            return this.jwtService.generate(authentificationDTO.username());
        }
        return null;
    }
    @GetMapping(path = "{id}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public Stream<UtilisateurDTO> getUtilisateurById(@PathVariable int id){
        return  this.utilisateurService.getUtilisateurById(id);
    }
    @GetMapping(path = "user", produces = MediaType.APPLICATION_JSON_VALUE)
    public Stream<UtilisateurDTO> getUtilisateur(){
        return this.utilisateurService.getUtilisateur();
    }
}
