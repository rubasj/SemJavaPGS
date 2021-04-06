import java.io.IOException;

/**
 * Class which represents barrier.
 * @author Jan Rubas
 * @version 1.1
 */
public class Barrier {

    /**
     * count of full lorries which can be filled ferry
     */
    private int countOfLorry;
    /** if thread sleep */
    private boolean toSleep = true;
    /** count of lorries */
    private int counter = 0;

    private long startTime = System.currentTimeMillis();

    /**
     * Class constructor of barrier
     * @param countOfLorry number of lorry which filled a ferry
     */
    public Barrier(int countOfLorry) {
        this.countOfLorry = countOfLorry;
    }

    /**
     * Method which synchronize threads
     */
    public synchronized void synchronize(boolean last){

        // if sleep == false, the thread is sleep
        while (!toSleep) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        counter++;


        if (counter == countOfLorry || last) {
            long end = System.currentTimeMillis();
            System.out.println((double) (end - Main.start) / 1000 + " Ferry went out.");
            try {
                Foreman.out.write(String.format("%.2f;Ferry;went out;%.2f\n", (double)(end-Main.start) / 1000, (double)(end - startTime)/1000));
            } catch (IOException e) {
                e.printStackTrace();
            }

            startTime  = System.currentTimeMillis();

            toSleep = false;
            notifyAll();
        }

        while (toSleep) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        counter--;

        if (counter == 0) {
            toSleep = true;
            notifyAll();
        }

    }
}


