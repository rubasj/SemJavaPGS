import java.util.Arrays;


/**
 * Main class
 * @author Jan Rubas
 */
public class Main {


    public static long start;

    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));

        start = System.currentTimeMillis();
        Foreman foreman = new Foreman(args); // instance of foreman


        try {
            foreman.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class CommandLineArgs {

    /**
     * number of workers
     */
    public static int cWorker;

    /**
     * Doba zpracovani
     */
    public static int tWorker;

    public static int capLorry;
    public static int tLorry;
    public static int capFerry;
    public static String inputFile;
    public static String outputFile;

    public CommandLineArgs(String[] args) {
        // Output file
        outputFile = args[3];
        // Input file
        inputFile = args[1];

        cWorker = Integer.parseInt(args[5]);
        tWorker = Integer.parseInt(args[7]);
        capLorry = Integer.parseInt(args[9]);
        tLorry = Integer.parseInt(args[9]);
        capFerry = Integer.parseInt(args[13]);
    }




}
