package alejandro.aplicacion.Infraestructura.Implementaciones.Validadores.Excepciones;

public class MontoNoPermitidoException extends RuntimeException{

    public MontoNoPermitidoException() {
        super();
    }

    public MontoNoPermitidoException(String message) {
        super(message);
    }
}
