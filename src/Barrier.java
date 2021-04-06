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
    public synchronized void synchronize(){

        // if sleep == false, the thread is sleep
        while (!toSleep) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        counter++;


        if (counter == countOfLorry) {
            // TODO generate new record to file
            System.out.println("Ferry went out.");

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


