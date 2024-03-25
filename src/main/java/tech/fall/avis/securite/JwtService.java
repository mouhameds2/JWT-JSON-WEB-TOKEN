package tech.fall.avis.securite;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tech.fall.avis.entite.Jwt;
import tech.fall.avis.entite.Utilisateur;
import tech.fall.avis.repository.JwtRepository;
import tech.fall.avis.service.UtilisateurService;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@AllArgsConstructor
@Service
public class JwtService {

    public static final String BEARER = "bearer";
    public static final String REFRESH = "refresh";
    public static final String TOKEN_INVALIDE = "Token invalide";
    //notre clé de crypto généré par le site https://randomgenerate.io/encryption-key-generator#google_vignette
    private final String ENCRIPTION_KEY = "9e55c91b68014f1b834617fef32be40138509f38a6ca2385f6419f148e0f5465";
    private UtilisateurService utilisateurService;
    private JwtRepository jwtRepository;

    public Jwt tokenByValue(String value) {
        return this.jwtRepository.findByValeurAndDesactivetedAndExpire(
                value,
                false,
                false
        ).orElseThrow(() -> new RuntimeException("Token invalide ou inconnu"));
    }
    public Map<String, String> generate(String username) {
        Utilisateur utilisateur = this.utilisateurService.loadUserByUsername(username);
        Map<String, String> jwtMap = this.generateJwt(utilisateur);

        //implementer pour la deconnexion de l'utilisateur on desactive le token. ici le token est actif et n'est pas expiré
        //on met le token dans la base de données en créant la classe Jwt et le service JwtService
        final Jwt jwt = Jwt.builder()
                .valeur(jwtMap.get(BEARER))
                .desactiveted(false)
                .expire(false)
                .utilisateur(utilisateur).build();
        this.jwtRepository.save(jwt);
        return jwtMap;
    }
    public void disableTokens(Utilisateur utilisateur){
        log.info("====== disableToke ==== " );
        final List<Jwt> jwtList = this.jwtRepository.findAllTokensUtilisateur(utilisateur.getEmail()).peek(
        jwt -> {
            jwt.setDesactiveted(true);
            jwt.setExpire(true);

        }).collect(Collectors.toList());
        this.jwtRepository.saveAll(jwtList);
        log.info("====== disableTokens ==== " +jwtList );

    }

    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }



    private Date getExpirationDateFromToken(String token) {
        return this.getClaim(token, Claims::getExpiration);
    }

    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //pour générer le jeton qu'on envoe à l'utilisateur

    private Map<String, String> generateJwt(Utilisateur utilisateur) {
        //date de création du jeton en millisecondes
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 30 * 60 * 1000;
        // les informations qu'il faut passer exemeple le nom
        final Map<String, Object> claims = Map.of(
                "nom", utilisateur.getNom(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, utilisateur.getEmail()
        );
//on stock notre clé dans une chaine de caractère
        final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(utilisateur.getEmail())
                .setClaims(claims)
                //clé de signature
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        //on retourne un map qui contient un baerer
        return Map.of(BEARER, bearer);
    }


    //Methode pour avoir notre clé pour pouvoir chiffré les données
    private Key getKey() {
        final byte[] decoder = Decoders.BASE64.decode(ENCRIPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }



    public void deconnexion() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("====== utilisateur ==== " +utilisateur);
        Jwt jwt = this.jwtRepository.findUtilisateurValidToken(utilisateur.getEmail(),
                false,
                false

        ).orElseThrow(() -> new RuntimeException("Token invalide") );
        jwt.setDesactiveted(true);
        jwt.setExpire(true);
        log.info("====== jwt ==== " +jwt.isExpire());
        System.out.println("====jwt ==== "+ jwt);
        this.jwtRepository.save(jwt);
    }




    //Methode pour supprimer les tokens tous les jours de façon automatique
   // @Scheduled(cron = "0 */1 * * * *") pour supprimer les tokens dans la BDD toutes les minutes
    @Scheduled(cron = "@daily")
    public void removeUselessJwt() {
       log.info("Suppression des token à {}", Instant.now());
        this.jwtRepository.deleteAllByExpireAndDesactiveted(true, true);
    }
}
