package ethicalengine;

/**
 * @description:
 * @author: Fan Jia
 */
public class InvalidDataFormatException extends Exception{
    public InvalidDataFormatException(int lineNumber) {
        super("WARNING: invalid data format in config file in line "+lineNumber);
    }
    public InvalidDataFormatException(String message) {
        super(message);
    }
}
