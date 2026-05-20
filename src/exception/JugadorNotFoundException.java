package src.exception;

public class JugadorNotFoundException extends LigaException {
    
    public JugadorNotFoundException(String identificador) {
        super("El jugador '" + identificador + "' no se encontró en la liga.");
    }

}