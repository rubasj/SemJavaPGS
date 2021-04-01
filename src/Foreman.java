import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Foreman implements Runnable {

    /**
     * List of blocks
     */
    private List<Integer> resources;

    /**
     * thread instance
     */
    public Thread thread;
    /**
     * output writer
     */
    public BufferedWriter out;


    /**
     * Count of workers without work (in the end of mining)
     */
    public static int withoutWork;

    private Worker[] workers;

    /**
     * Constructor
     * Initialize class parameters and switch new thread
     *
     * @param args command lines params
     */
    public Foreman(String[] args) {

        try {

            // Init Output file
            FileWriter fw = new FileWriter(CommandLineArgs.outputFile);
            out = new BufferedWriter(fw);
            out.write("Output file = \"" + CommandLineArgs.outputFile + "\".\n");
            System.out.println("Output file = \"" + CommandLineArgs.outputFile + "\".");

            // init Input file
            System.out.println("Input file = \"" + CommandLineArgs.inputFile + "\".");
            out.write("Input file = \"" + CommandLineArgs.inputFile + "\"." + "\n");
            initResources(CommandLineArgs.inputFile);

            System.out.println("Predak - start simulace." + "\n");

            out.write("Predak - start simulace.\nPredak - inicializace promennych z prikazoveho radku." + "\n");

            System.out.println("Predak - inicializace promennych z prikazoveho radku.");

            System.out.println("Pocet delniku: " + CommandLineArgs.cWorker);
            out.write("Pocet delniku: " + CommandLineArgs.cWorker + "\n");
            workers = new Worker[CommandLineArgs.cWorker];

            System.out.println("Delka zpracovani bloku: " + CommandLineArgs.tWorker);
            out.write("Delka zpracovani bloku: " + CommandLineArgs.tWorker + "\n");

            System.out.println("Kapacita nakladaku: " + CommandLineArgs.capLorry);
            out.write("Kapacita nakladaku: " + CommandLineArgs.capLorry + "\n");


            System.out.println("Cas jizdy nakladaku: " + CommandLineArgs.tLorry + "\n");
            out.write("Cas jizdy nakladaku: " + CommandLineArgs.tLorry);


            System.out.println("Kapacita lodi: " + CommandLineArgs.capFerry);
            out.write("Kapacita lodi: " + CommandLineArgs.capFerry + "\n");
        } catch (NumberFormatException | IOException e) {
            System.err.println("Wrong parameter -capFerry");
            System.exit(1);
        }
        thread = new Thread(this);      // thread init
        thread.start(); // starting


    }


    /**
     * Main method witch is being used after starting the Thread of Foreman from constructor.
     * Method create Worker instances.
     */
    @Override
    public void run() {

        try {
            out.write("Předák - vytváření dělníků.\n");
            System.out.println("Předák - vytváření dělníků.");

            /* creating workers  */
            for (int i = 0; i < CommandLineArgs.cWorker; i++) {
                workers[i] = new Worker("Delnik" + (i + 1), this, CommandLineArgs.tWorker);
            }

            try {

                for (Worker worker :
                        workers) {
                    worker.thread.join();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            out.close(); // close output file
        } catch (IOException e) {
            System.err.println("Problems with buffered writer.");
        }
    }

    /**
     * Method gives the blocks to be mined
     * It is critical section, because Workers are threads which can call these method at the same moment.
     */
    public synchronized int getResources(String name) {

        if (!resources.isEmpty()) {
            return resources.remove(0);

        }

        System.out.println(name + " - neni uz co tezit.\n");
        try {
            out.write(name + " - neni uz co tezit.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // the worker dont have any next job
        withoutWork++;
        return -1;
    }

    /**
     * Foreman initialize resources.
     *
     * @param inputFile input
     */
    private void initResources(String inputFile) {
        resources = new ArrayList<>(); // list of resources
        int blocksCount = 0;    //number of block in a resource
        FileReader fr = null;
        try {
            fr = new FileReader(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader in = new BufferedReader(fr);

        int ch;
        try {
            while ((ch = in.read()) != -1 || blocksCount > 0) {
                if (ch == 'x') {
                    blocksCount++;
                    continue;
                }
                if (blocksCount > 0) {      // if char from file != 'x', the number of the
                    // current resource block will be added to list of resources like one resource
                    // and blocksCount will be set to zero.
                    resources.add(blocksCount);
                    blocksCount = 0;
                }
            }
            System.out.println(resources.toString());

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getWithoutWork() {
        return withoutWork;
    }
}
