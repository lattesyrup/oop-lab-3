package exceptions;

public class NoSuchVehicleTypeException extends Exception {
    public NoSuchVehicleTypeException(String message) {
        super(message);
    }
}