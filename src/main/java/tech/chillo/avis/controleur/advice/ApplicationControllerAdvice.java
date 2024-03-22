package tech.chillo.avis.controleur.advice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.chillo.avis.entite.ErrorEntity;

@ControllerAdvice
public class ApplicationControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({EntityNotFoundException.class})
    public @ResponseBody ErrorEntity handleException(EntityNotFoundException exception){
        return new ErrorEntity(null,exception.getMessage());
    }
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({RuntimeException.class})
    public @ResponseBody ErrorEntity handleException(RuntimeException exception){
        return new ErrorEntity(null,exception.getMessage());
    }




}

