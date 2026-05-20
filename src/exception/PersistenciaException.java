package src.exception;

public class PersistenciaException extends LigaException{
    
    /**
     * Excepción personalizada para indicar que ha ocurrido un error relacionado con la persistencia de datos, como problemas al guardar o cargar información de equipos, jugadores o partidos.
     */
    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}
