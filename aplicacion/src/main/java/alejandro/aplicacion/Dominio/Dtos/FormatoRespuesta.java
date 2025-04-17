package alejandro.aplicacion.Dominio.Dtos;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;

public record FormatoRespuesta(
        LocalDateTime timeStamp,
        Integer status,
        String mensaje
) {
    public FormatoRespuesta(FieldError fieldError){
        this(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                fieldError.getField() + " - " + fieldError.getDefaultMessage()
        );
    }
}
