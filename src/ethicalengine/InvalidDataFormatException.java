package ethicalengine;

/**
 * @description:
 * @author: Fan Jia
 */
public class InvalidDataFormatException extends Exception{
    public InvalidDataFormatException() {
        super("WARNING: invalid data format in config file in line ");
    }
}
