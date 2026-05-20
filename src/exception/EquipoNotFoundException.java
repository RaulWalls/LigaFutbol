package src.exception;

public class EquipoNotFoundException extends LigaException {
    
    public EquipoNotFoundException(String nombreEquipo) {
        super("El equipo '" + nombreEquipo + "' no se encontró en la liga.");
    }
}
