package ethicalengine;

/**
 * @description: handle InvalidDataFormatException
 * @author: Fan Jia
 */
public class InvalidDataFormatException extends Exception{
    public InvalidDataFormatException() {
        super("WARNING: invalid data format in config file in line ");
    }
}
