package src.exception;

public class PartidoInvalidoException extends LigaException {
    
    /**
     * Excepción personalizada para indicar que se ha intentado registrar un partido con datos inválidos, como equipos no registrados, fecha en formato incorrecto o resultados no válidos.
     */
    public PartidoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
