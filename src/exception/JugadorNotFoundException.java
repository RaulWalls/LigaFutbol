package src.exception;

public class JugadorNotFoundException extends LigaException {
    
    /**
     * Excepción personalizada para indicar que se ha intentado acceder o modificar un jugador que no existe en la liga.
     */
    public JugadorNotFoundException(String identificador) {
        super("El jugador '" + identificador + "' no se encontró en la liga.");
    }

}