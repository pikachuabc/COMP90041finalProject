package proj.ethicalengine;

/**
 * @description:
 * @author: Fan Jia
 */
public class InvalidInputException extends Exception {
    public InvalidInputException() {
        super("Invalid response. ");
    }
    public InvalidInputException(String message) {
        super(message);
    }
}
