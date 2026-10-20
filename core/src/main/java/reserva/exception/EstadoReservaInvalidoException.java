package reserva.exception;

public class EstadoReservaInvalidoException extends RuntimeException {
    public EstadoReservaInvalidoException(String message) {
        super(message);
    }
}