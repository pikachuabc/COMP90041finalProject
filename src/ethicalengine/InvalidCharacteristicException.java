package ethicalengine;

/**
 * @description: handle invalid characteristic input
 * @author: Fan Jia
 */
public class InvalidCharacteristicException extends Exception {
    public InvalidCharacteristicException(int lineNumber) {
        super("WARNING: invalid data format in config file in line "+lineNumber);
    }
    public InvalidCharacteristicException(String message) {
        super(message);
    }
}
