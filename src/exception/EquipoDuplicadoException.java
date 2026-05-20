package src.exception;

public class EquipoDuplicadoException extends LigaException {
    
    public EquipoDuplicadoException(String nombreEquipo) {
        super("El equipo '" + nombreEquipo + "' ya está registrado.");
    }

}
