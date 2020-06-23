package ethicalengine;

/**
 * @description: handle InvalidInputException
 * @author: Fan Jia
 */
public class InvalidInputException extends Exception {
    public InvalidInputException() {
        super("Invalid response. ");
    }
}