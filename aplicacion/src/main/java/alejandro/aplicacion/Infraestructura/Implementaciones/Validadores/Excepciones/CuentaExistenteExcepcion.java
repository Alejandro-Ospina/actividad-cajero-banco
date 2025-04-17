package alejandro.aplicacion.Infraestructura.Implementaciones.Validadores.Excepciones;

public class CuentaExistenteExcepcion extends RuntimeException{
    public CuentaExistenteExcepcion() {
        super();
    }

    public CuentaExistenteExcepcion(String message) {
        super(message);
    }
}
