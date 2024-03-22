package tech.chillo.avis.controleur;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.chillo.avis.dto.AuthentificationDTO;
import tech.chillo.avis.dto.UtilisateurDTO;
import tech.chillo.avis.entite.Utilisateur;
import tech.chillo.avis.securite.JwtService;
import tech.chillo.avis.service.UtilisateurService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

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
