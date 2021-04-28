/**
 * Class witch represents ferry
 * @author Jan Rubas
 * @version 1.2
 */
public class Ferry extends Lorry {

    /** barrier */
    static Barrier barrier;

    /**
     * Class constructor.
     */
    public Ferry() {
        // max capacity of ferry
        int maxCapacity = CommandLineArgs.capFerry;
        barrier = new Barrier(maxCapacity);
    }

    /** load lorry capacity to ferry */
    public void loadOnFerry(Lorry lorry) {
        if (lorry.getCurrCapacity() == lorry.getMaxCapacity()) barrier.synchronize(false);

        // if last lorry isn't full
        if (lorry.getCurrCapacity() != lorry.getMaxCapacity() && lorry.isLast) barrier.synchronize(true);
    }
}
