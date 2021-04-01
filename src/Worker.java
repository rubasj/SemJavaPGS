import java.io.IOException;
import java.util.Random;

/**
 * Class worker represents Worker
 * @author Jan Rubas
 */
public class Worker implements Runnable{


    public Thread thread;
    private final String name;
    private final int tWorkerMax;

    public static Lorry lorry = new Lorry();
    public final Foreman foreman;

    public Worker(String name, Foreman foreman, int tWorkerMax) {
        this.tWorkerMax = tWorkerMax;
//        this.blocksCountSum = 0;
        this.foreman = foreman;
        this.name = name;
        thread = new Thread(this, name);

        thread.start();
    }



    @Override
    public void run() {


        int blocksCount;
        // is there more resources
        while ((blocksCount = foreman.getResources(thread.getName())) != -1) {  // if is returned -1, the resources were mined
            System.out.println(thread.getName() + " nese " + blocksCount + " zdroje.");
            try {
                foreman.out.write(thread.getName() + " nese " + blocksCount + " zdroje.\n");

                //////////
                // start mining resource
                long startMiningRes = System.currentTimeMillis();
                miningResource(blocksCount);
                long endMiningRes = System.currentTimeMillis();
                foreman.out.write(String.format("%.2f;%s;resource mined;%.2f\n", (double)(endMiningRes-Main.start)/1000,
                        this.name, (double)(endMiningRes-startMiningRes)/1000));
                //////////

                //////////
                // load on lorry
                boolean lastBlock = false;
                for (int i = 0; i < blocksCount; i++) {
                    if (i == blocksCount-1) lastBlock = true;
                    // load source by source
                    if (!lorry.loadOnLorry(this, lastBlock)) i--;
                }

            if (blocksCount == foreman.getWithoutWork() && lorry.getCurrCapacity() != 0) {


                if(!lorry.thread.isAlive()) {
                    lorry.run();
                }
            }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    private void miningResource(int blocksCount) throws InterruptedException, IOException {
        Random r = new Random();
        int waitForMining;
        for (int i = 0; i < blocksCount; i++) {
            waitForMining = r.nextInt(tWorkerMax);
            long startMiningBlock = System.currentTimeMillis();
            // interval (0;tWorker>
            Thread.sleep(waitForMining);
            long endMiningBlock = System.currentTimeMillis();
            String data = String.format("%.2f;%s;block mined;%.2f\n", (double)(endMiningBlock-Main.start)/1000,
                    this.name, (double)(endMiningBlock-startMiningBlock)/1000);
            foreman.out.write(data);
        }
    }
}
