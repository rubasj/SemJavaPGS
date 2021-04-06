import java.util.Arrays;


/**
 * Main class
 * @author Jan Rubas
 * @version 1.0
 */
public class Main {

    /** start simulation */
    public static long start;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));

        CommandLineArgs arguments = new CommandLineArgs(args);
        start = System.currentTimeMillis();
        Foreman foreman = new Foreman(); // instance of foreman


        try {
            foreman.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

/**
 * Class witch initialize program arguments
 * @author Jan Rubas
 * @version 1.2
 */
class CommandLineArgs {

    /**
     * number of workers
     */
    public static int cWorker;

    /**
     * Doba zpracovani
     */
    public static int tWorker;

    /** Lorry capacity */
    public static int capLorry;
    /** Lorry max time to traveling to END and Ferry */
    public static int tLorry;
    /** Ferry capacity */
    public static int capFerry;
    /** input file */
    public static String inputFile;
    /** output file */
    public static String outputFile;

    /**
     * Init program parameters.
     * @param args arguments in array
     */
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
