package ethicalengine;

/**
 * @description: handle InvalidCharacteristicException
 * @author: Fan Jia
 */
public class InvalidCharacteristicException extends Exception {
    public InvalidCharacteristicException() {
        super("WARNING: invalid characteristic in config file in line ");
    }
}
