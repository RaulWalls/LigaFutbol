package src.exception;

public class EquipoNotFoundException extends LigaException {
    
    /**
     * Excepción personalizada para indicar que se ha intentado acceder o modificar un equipo que no existe en la liga.
     */
    public EquipoNotFoundException(String nombreEquipo) {
        super("El equipo '" + nombreEquipo + "' no se encontró en la liga.");
    }
}
