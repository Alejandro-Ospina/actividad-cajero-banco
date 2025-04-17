package alejandro.aplicacion.Aplicacion.Excepciones;

import alejandro.aplicacion.Dominio.Dtos.FormatoRespuesta;
import alejandro.aplicacion.Infraestructura.Implementaciones.Validadores.Excepciones.CuentaExistenteExcepcion;
import alejandro.aplicacion.Infraestructura.Implementaciones.Validadores.Excepciones.EntradaNoCoincidenteExcepcion;
import alejandro.aplicacion.Infraestructura.Implementaciones.Validadores.Excepciones.MontoNoPermitidoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ManejadorDeExcepcionesGlobales {

    @ExceptionHandler (CuentaExistenteExcepcion.class)
    public ResponseEntity<?> manejarCuentaExistenteExcepcion(CuentaExistenteExcepcion ex){
        return ResponseEntity.badRequest().body(
                new FormatoRespuesta(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<?> manejarArgumentoDeMetodoExcepcion(MethodArgumentNotValidException ex){
        var errores = ex.getFieldErrors().stream().map(FormatoRespuesta::new).toList();
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler (EntityNotFoundException.class)
    public ResponseEntity<?> manajerEntidadesNoEncontradas(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new FormatoRespuesta(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler (EntradaNoCoincidenteExcepcion.class)
    public ResponseEntity<?> manejarEntradaNoCoincidenteException(EntradaNoCoincidenteExcepcion ex){
        return ResponseEntity.badRequest().body(
                new FormatoRespuesta(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler (MontoNoPermitidoException.class)
    public ResponseEntity<?> manejarMontoNoPermitidoException(MontoNoPermitidoException ex){
        return ResponseEntity.badRequest().body(
                new FormatoRespuesta(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler (UsernameNotFoundException.class)
    public ResponseEntity<?> manejarUsernameNotFound(UsernameNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new FormatoRespuesta(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler (RuntimeException.class)
    public ResponseEntity<?> manejarRuntimeException(RuntimeException ex){
        return ResponseEntity.badRequest().body(
                new FormatoRespuesta(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler (Exception.class)
    public ResponseEntity<?> maneajarException(Exception ex){
        return ResponseEntity.internalServerError().body(
                new FormatoRespuesta(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage()
                )
        );
    }
}
