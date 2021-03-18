import java.io.IOException;
import java.util.Random;

public class Worker implements Runnable{


    private int blocksCountSum;
    private Thread th;

    private int tWorkerMax;

    private Chief chief;

    public Worker(String name, Chief chief, int tWorkerMax) {
        this.tWorkerMax = tWorkerMax;
        this.blocksCountSum = 0;
        this.chief = chief;
        th = new Thread(this, name);

        th.start();
    }



    @Override
    public void run() {
        Random r = new Random();

        int blocksCount;


        System.out.println();
        while ((blocksCount = chief.getSources(th.getName())) != -1) {
            System.out.println(th.getName() + " nese " + blocksCount + " zdroje.");
            try {
                chief.out.write(th.getName() + " nese " + blocksCount + " zdroje.\n");

                int count = 0;
                for(int i = 0; i < blocksCount; i++) {
                    Thread.sleep(1+ r.nextInt(tWorkerMax));
                }



            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }


    }
}
