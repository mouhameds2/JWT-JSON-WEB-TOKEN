package tech.fall.avis.controleur.advice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tech.fall.avis.entite.ErrorEntity;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Map;



@Slf4j
@RestControllerAdvice
public class ApplicationControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({EntityNotFoundException.class})
    public @ResponseBody ErrorEntity handleException(EntityNotFoundException exception){
        return new ErrorEntity(null,exception.getMessage());
    }
    /*@ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({RuntimeException.class})
    public @ResponseBody ErrorEntity handleException(RuntimeException exception){
        return new ErrorEntity(null,exception.getMessage());
    }

     */

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = Exception.class)
    public Map<String,String> exceptionsHandeler(){
        return Map.of("Erreur","Erreur non identifié merci de contacter l'administrateur");
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = BadCredentialsException.class)
    public @ResponseBody ProblemDetail badCredentialsException(final BadCredentialsException exception){
        log.error("======== Erreur de connexion ====== "+exception.getMessage(),exception);
        final  ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Identifiant incorrect");
        problemDetail.setProperty("Erreur","Votre login ou votre mot de passe est incorrecte");
        return problemDetail;
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {MalformedJwtException.class, SignatureException.class})
    public @ResponseBody ProblemDetail signatureException(final Exception exception){
        log.error("======== Token inconnu ====== "+exception.getMessage(),exception);
        final  ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Token inconnu");
        problemDetail.setProperty("Erreur","Votre token est invalid");
        return problemDetail;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public @ResponseBody ProblemDetail methodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception){
        log.error("======== Saisie incorrecte ====== "+exception.getMessage(),exception);
        final  ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                "Vous avez saisi une chaine de caractère merci de saisir un chiffre");
        problemDetail.setProperty("Erreur","l'utilisateur que vous chercher n'existe pas");
        return problemDetail;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = UserPrincipalNotFoundException.class)
    public @ResponseBody ProblemDetail userPrincipalNotFoundException(final UserPrincipalNotFoundException exception){

        final  ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                "veiller saisir un autre utilisateur");
        problemDetail.setProperty("Erreur","L'utlisateur que vous chercher n'existe pas");
        return problemDetail;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = AccessDeniedException.class)
    public @ResponseBody ProblemDetail accessDeniedException(final AccessDeniedException exception){
        log.error("======== Problème de permission ====== "+exception.getMessage(),exception);
        final  ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                "Problème de permission");
        problemDetail.setProperty("Erreur","Vous n'avez pas les droits d'accès");
        return problemDetail;
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = ExpiredJwtException.class)
    public @ResponseBody ProblemDetail expiredJwtException(final ExpiredJwtException exception){
        log.error("======== Token Expiré ====== "+exception.getMessage(),exception);
        final  ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Votre n'est plus valide");
        problemDetail.setProperty("Erreur","Votre token est expiré");
        return problemDetail;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = RuntimeException.class)
    public @ResponseBody ProblemDetail runtimeException(final RuntimeException exception){
        log.error("======== Votre Compte existe existe ====== "+exception.getMessage(),exception);
        final  ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "vous avez  déja un compte merci de vous conecter ou de réinitialiser votre mot de passe **");
        problemDetail.setProperty("Erreur","vous avez  déja un compte");
        return problemDetail;
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value = MailAuthenticationException.class)
    public @ResponseBody ProblemDetail mailAuthenticationException(final MailAuthenticationException exception){
        log.error("======== mail invalid ====== "+exception.getMessage(),exception);
        final  ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE,
                "Votre email est invalid");

        return problemDetail;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = InvalidKeyException.class)
    public @ResponseBody ProblemDetail codeValidationInvalide(final InvalidKeyException exception){
        log.error("======== Code Validation invalid ====== "+exception.getMessage(),exception);
        final  ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                "Votre code Validation invalid");

        return problemDetail;
    }



}

