package tech.fall.avis.controleur;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public List<Avis> liste() {
        return this.avisService.liste();
    }

}
