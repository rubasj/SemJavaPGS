import java.io.IOException;
import java.util.Random;

public class Lorry implements Runnable{

    private static Ferry ferry = new Ferry();

    private int tLorryMax;
    private int maxCapacity;
    private int currCapacity = 0;
    public Thread thread;
    public long loadTimeStart = System.currentTimeMillis();
    public static int inTheEnd = 0;

    public Lorry() {

        thread = new Thread(this);
        maxCapacity = CommandLineArgs.capLorry;
        tLorryMax = CommandLineArgs.tLorry;
    }


    public synchronized boolean loadOnLorry(Worker worker, boolean lastResource) {
        if (maxCapacity == currCapacity) return false;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.currCapacity++;

        if (this.currCapacity == this.maxCapacity) {
            if (this.thread.getState() == Thread.State.NEW) {
                thread.start();


                long end = System.currentTimeMillis();
                try {
                    worker.foreman.out.write(String.format("%.2f;%s;%d;Lorry is full;%.2f\n",
                            (double) (end - Main.start) / 1000, "Lorry", thread.getId(), (double) (end - this.loadTimeStart) / 1000));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!(Foreman.withoutWork == CommandLineArgs.cWorker && lastResource)) {
                    Worker.lorry = new Lorry();
                    return true;
                }
            }else {
                return false;
            }
        }

        return true;
    }

    @Override
    public void run() {
        Random r = new Random();

        int tCurrLorry = r.nextInt(tLorryMax);
        try {
            long start  = System.currentTimeMillis();
            Thread.sleep(tCurrLorry);
            long end = System.currentTimeMillis();
            // TODO write to file
            System.out.printf("%.2f;%s;%d;Lorry arrived to ferry;%.2f", (double)(end-Main.start) / 1000, "Lorry", thread.getId(), (double)(end - start)/1000);


        ferry.loadOnFerry(this);
        inTheEnd++;

        tCurrLorry = r.nextInt(tLorryMax);


            start = System.currentTimeMillis();
            Thread.sleep(tCurrLorry);
            end = System.currentTimeMillis();

            System.out.printf("%.2f;%s;%d;Lorry arrived to END;%.2f", (double)(end-Main.start) / 1000, "Lorry", thread.getId(), (double)(end - start)/1000);
        } catch (InterruptedException e) {
        e.printStackTrace();
    }
    }

    public int getCurrCapacity() {
        return currCapacity;
    }
}
