package src.exception;

public class EquipoDuplicadoException extends LigaException {
    
    /**
     * Excepción personalizada para indicar que se ha intentado agregar un equipo con un nombre que ya existe en la liga.
     */
    public EquipoDuplicadoException(String nombreEquipo) {
        super("El equipo '" + nombreEquipo + "' ya está registrado.");
    }

}
