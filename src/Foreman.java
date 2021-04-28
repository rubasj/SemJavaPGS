import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    public static BufferedWriter out;

    /** exception problem  - output */
    public static final String OUT_EXCEPTION = "Output: Writing fault";


    /**
     * Count of workers without work (in the end of mining)
     */
    private static int withoutWork;

    private Worker[] workers;
    public static List<Lorry> lorries;
    private FileWriter fw;
    /**
     * Constructor
     * Initialize class parameters and switch new thread
     *
     */
    public Foreman() {

        try {

            // Init Output file\
            System.out.println(CommandLineArgs.outputFile);
            fw = new FileWriter(CommandLineArgs.outputFile);
            out = new BufferedWriter(fw);
            System.out.println("Output file = \"" + CommandLineArgs.outputFile + "\".");

            // init Input file
            System.out.println("Input file = \"" + CommandLineArgs.inputFile + "\".");



            System.out.println("Count of workers: " + CommandLineArgs.cWorker);
            workers = new Worker[CommandLineArgs.cWorker];

            lorries = new ArrayList<>();
            System.out.println("Max time for mining one block: " + CommandLineArgs.tWorker);

            System.out.println("Lorry capacity: " + CommandLineArgs.capLorry);

            System.out.println("Max lorry time for achieve destination: " + CommandLineArgs.tLorry);

            System.out.println("Ferry capacity: " + CommandLineArgs.capFerry);
            System.out.println();
            initResources(CommandLineArgs.inputFile);
        } catch (NumberFormatException e) {
            System.err.println("Some parameter is wrong.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Output: Output file does not loaded.");
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

            out.write("\n########### LEGEND ###########\n");
            out.write("# For workers\n#\t <runtime>;<workername>;<action>;<time for action>\n");
            out.write("# For lorries\n#\t <runtime>;<lorryname>;<action>;<time for action>\n");
            out.write("# For ferry\n#<runtime>;<Ferry>;<action>;<time for filling>\n");


            /* creating workers  */
            for (int i = 0; i < CommandLineArgs.cWorker; i++) {
                workers[i] = new Worker("Worker[" + (i + 1)+ "]", this, CommandLineArgs.tWorker);
            }

            try {
                // lorry list is empty, we have to add first lorry before simulation
                newLorry();
                for (Worker worker :
                        workers) {
//                    System.out.println(worker.thread.getName() + " mined " + worker.getTotalCountOfMinedBlocks() + " blocks.");
                    worker.thread.join();

                }

                //the last lorry
                if (lorries.get(lorries.size() - 1).getCurrCapacity() > 0 &&  !lorries.get(lorries.size()-1).thread.isAlive()) {
                    lorries.get(lorries.size() - 1).isLast = true;
                    lorries.get(lorries.size() - 1).thread.start();
                }

                AtomicInteger totalMinedBlocks = new AtomicInteger();
                lorries.forEach(lorry -> {
                    try {
                        totalMinedBlocks.addAndGet(lorry.getCurrCapacity());
                        lorry.thread.join();
                    } catch (InterruptedException e) {
                        System.err.println("Lorry " + lorry  + " does not canceled.");
                    }
                });
                System.out.println("Total mined blocks which arrived to goal: " + totalMinedBlocks.get());

            } catch (InterruptedException e) {
                System.err.println("Foreman: interrupted exception.");
            }


            out.close(); // close output file
            fw.close();
        } catch (IOException e) {
            System.err.println("Writer: Problem in foreman.");
        }
    }

    /**
     * Method gives the blocks to be mined
     * It is critical section, because Workers are threads which can call these method at the same moment.
     */
    public synchronized int getResources() {

        if (!resources.isEmpty()) {
            return resources.remove(0);

        }
        // the worker dont have any next job
        withoutWork++;
        return -1;
    }

    /**
     * Foreman initialize resources.
     * @param inputFile input
     */
    private void initResources(String inputFile) throws IOException {
        resources = new ArrayList<>(); // list of resources
        int blocksCount = 0;    //number of block in a resource
        FileReader fr = null;
        try {
            fr = new FileReader(inputFile);
        } catch (FileNotFoundException e) {
            System.err.println("Initialize resources: " + inputFile + " does not exist.");
        }

        BufferedReader in = new BufferedReader(fr);

        int ch;
        int totalCountOfBlocks = 0;
        try {
            while ((ch = in.read()) != -1 || blocksCount > 0) {
                if (ch == 'X') {
                    blocksCount++;
                    continue;
                }
                if (blocksCount > 0) {      // if char from file != 'x', the number of the
                    // current resource block will be added to list of resources like one resource
                    // and blocksCount will be set to zero.
                    totalCountOfBlocks += blocksCount;
                    resources.add(blocksCount);
                    blocksCount = 0;
                }
            }
            System.out.println("Count of resources: " + resources.size());
            System.out.println("Count of blocks: " + totalCountOfBlocks);
            out.write("\n# Input file analysis");
            out.write("\n# Count of resources: " + resources.size());
            out.write("\n# Count of blocks: " + totalCountOfBlocks);

            in.close();
        } catch (IOException e) {
            System.err.println("Initialize resources: Problem with Input / Output file.");
        } finally {
            in.close();
        }
    }


    /**
     * Add new lorry to list
      */
    public synchronized static void newLorry() {
        lorries.add(lorries.size(), new Lorry());
    }

    /**
     * Get count of workers without work
     * @return count of workers without work
     */
    public static int getWithoutWork() {
        return withoutWork;
    }

}
