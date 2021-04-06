import java.io.IOException;
import java.util.Random;

public class Lorry implements Runnable{

    /** Ferry for resources */
    private static Ferry ferry = new Ferry();

    /** Max lorry time, which can be reached for distance of ferry */
    private final int tLorryMax;

    /** Max lorry capacity of blocks */
    private final int maxCapacity;
    /** current lorry capacity */
    private int currCapacity = 0;
    /** Lorry's thread */
    public Thread thread;
    /** time when lorry started to distance */
    public long loadTimeStart = System.currentTimeMillis();
    /** count of lorries, which reached END */
    public static int inTheEnd = 0;

    /** Number of created lorries, index is part of name, first lorry is 1 */
    public static int numberOfLorries = 1;

    /** Lorry name */
    private String name;

    /**
     * Class constructor fill instance params.
     */
    public Lorry() {

        this.name = "Lorry" + numberOfLorries;
        thread = new Thread(this);
        maxCapacity = CommandLineArgs.capLorry;
        tLorryMax = CommandLineArgs.tLorry;
    }


    /**
     * Synchronized metod for loading blocks to lorry
     * @param worker worker, who load block to lorry
     * @param lastBLock if current block is last
     * @return if loading was success, return true
     */
    public synchronized boolean loadOnLorry(Worker worker, boolean lastBLock) throws InterruptedException {
        // capacity is full
        if (maxCapacity == currCapacity) return false;

        // time to transfer to lorry
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.currCapacity++;

        // if lorry is full
        if (this.currCapacity == this.maxCapacity) {
            // new thread can be started
            if (this.thread.getState() == Thread.State.NEW) {
                this.thread.start();

                long end = System.currentTimeMillis();
                try {
                    worker.foreman.out.write(String.format("%.2f;%s;Lorry is full;%.2f\n",
                            (double) (end - Main.start) / 1000, name, (double) (end - this.loadTimeStart) / 1000));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // if  without work !=  cWorker && !lastResource
                if (!(worker.foreman.getWithoutWork() == CommandLineArgs.cWorker && lastBLock)) {
                    numberOfLorries++;
                    Worker.lorry = new Lorry();
                    return true;
                }
            }else {
                return false;
            }
        }

        return true;
    }

    /***
     * Runnable method for starting Lorry thread instance.
     */
    @Override
    public void run() {
        Random r = new Random();

        // time for scheduling to the distance
        int tCurrLorry = r.nextInt(tLorryMax);
        try {
            long start  = System.currentTimeMillis();
            Thread.sleep(tCurrLorry);
            long end = System.currentTimeMillis();
            // TODO write to file
            System.out.printf("%.2f;%s;Lorry arrived to ferry;%.2f\n", (double)(end-Main.start) / 1000, name, (double)(end - start)/1000);

            ferry.loadOnFerry(this);


            // go to END
            tCurrLorry = r.nextInt(tLorryMax);
            inTheEnd++;

            start = System.currentTimeMillis();
            Thread.sleep(tCurrLorry);
            end = System.currentTimeMillis();
            System.out.printf("%.2f;%s;Lorry arrived to END;%.2f\n", (double)(end-Main.start) / 1000, name, (double)(end - start)/1000);
        } catch (InterruptedException e) {
        e.printStackTrace();
    }
    }

    /**
     * Getter for current capacity
     * @return current capacity
     */
    public int getCurrCapacity() {
        return currCapacity;
    }

    /**
     *  Getter for max capacity
     * @return max capacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }
}
