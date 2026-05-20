package src.exception;

public class LigaException extends Exception {
    
    /**
     * Excepción base para errores relacionados con la lógica de la liga. Esta clase se utiliza como superclase 
     * para otras excepciones específicas de la liga, como EquipoDuplicadoException, EquipoNotFoundException y JugadorNotFoundException.
     */
    public LigaException(String message) {
        super(message);
    }

    // Constructor adicional para permitir incluir una causa subyacente al lanzar la excepción.
    public LigaException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
