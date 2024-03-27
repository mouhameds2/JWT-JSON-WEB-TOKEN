package tech.fall.avis.controleur;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.fall.avis.entite.Avis;
import tech.fall.avis.service.AvisService;

import java.util.List;

@AllArgsConstructor
@RequestMapping("avis")
@RestController
public class AvisControleur {
    private final AvisService avisService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void creer(@RequestBody Avis avis) {
        this.avisService.creer(avis);
    }

    //Methode pour deonner accès uniquement aux Manager et aux Admin
    // @PreAuthorize("hasAuthority( 'ROLE_ADMINISTRATEUR')") accès pour uniquement les admin
    @PreAuthorize("hasAnyAuthority('MANAGER_READ', ' ADMINISTRATEUR_READ')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Avis> liste() {
        return this.avisService.liste();
    }

}
