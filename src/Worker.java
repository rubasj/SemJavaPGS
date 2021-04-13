import java.io.IOException;
import java.util.Random;

/**
 * Class worker represents Worker
 * @author Jan Rubas
 */
public class Worker implements Runnable{

    /** total count of blocks */
    private int totalCountOfMinedBlocks = 0;

    /** thread */
    public Thread thread;

    /** Worker's name */
    private final String name;
    /** max time for mining */
    private final int tWorkerMax;

    /** foreman */
    public final Foreman foreman;

    /**
     * Constructor
     * @param name worker name
     * @param foreman foreman
     * @param tWorkerMax max time for working
     */
    public Worker(String name, Foreman foreman, int tWorkerMax) {
        this.tWorkerMax = tWorkerMax;
        this.foreman = foreman;
        this.name = name;
        thread = new Thread(this, name);

        thread.start();
    }


    /**
     * Method for pararel thread.
     */
    @Override
    public void run() {

        int blocksCount;
        // is there more resources
        while ((blocksCount = foreman.getResources(thread.getName())) != -1) {  // if is returned -1, the resources were mined
//            System.out.println(thread.getName() + " carries " + blocksCount + " blocks.");
            try {
//                Foreman.out.write(thread.getName() + " carries " + blocksCount + " blocks.\n");

                //////////
                // start mining resource
                long startMiningRes = System.currentTimeMillis();
                miningResource(blocksCount);
                long endMiningRes = System.currentTimeMillis();
                Foreman.out.write(String.format("%.2f;%s;resource mined;%.2f\n", (double)(endMiningRes-Main.start)/1000,
                        this.name, (double)(endMiningRes-startMiningRes)/1000));
                //////////

                //////////
                // load on lorry
                boolean lastBlock = false;
                for (int i = 0; i < blocksCount; i++) {
                    if (i == blocksCount-1) lastBlock = true;
                    // if lorry list is empty
                    if (foreman.lorries.isEmpty()) {
                        foreman.lorries.add(new Lorry());
                    }
                    // load source by source
                    if (!foreman.lorries.get(foreman.lorries.size()-1).loadOnLorry(this, lastBlock)) i--;
                }

                totalCountOfMinedBlocks += blocksCount;

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * Method, which will be simulate mining one resource
     * @param blocksCount count of blocks in resource
     */
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
           // System.out.println(data);
            foreman.out.write(data);
        }
    }



}
