import proj.ethicalengine.*;

/**
 * @description:test
 * @author: Fan Jia
 */
public class test {
    public static void main(String[] args) {
        for (String string : args) {
            System.out.println(string);
        }
    }

    public static String ratio() {
        return String.format("%.2f", (double) 2 / 3);
    }
}
