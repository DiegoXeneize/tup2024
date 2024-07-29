package ar.edu.utn.frbb.tup.model.exception;

public class TipoCuentaAlreadyExistsException extends Throwable {
    public TipoCuentaAlreadyExistsException(String message) {
        super(message);
    }

    public static class NoAlcanzaException extends Throwable {
    }

    public static class CantidadNegativaException extends Throwable {
    }
}
