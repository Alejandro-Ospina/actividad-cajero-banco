package alejandro.aplicacion.Infraestructura.Implementaciones.Validadores.Excepciones;

public class EntradaNoCoincidenteExcepcion extends RuntimeException{

    public EntradaNoCoincidenteExcepcion() {
        super();
    }

    public EntradaNoCoincidenteExcepcion(String message) {
        super(message);
    }
}
