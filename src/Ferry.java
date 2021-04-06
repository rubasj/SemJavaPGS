/**
 * Class witch represents ferry
 * @author Jan Rubas
 * @version 1.2
 */
public class Ferry {

    /** barrier */
    static Barrier barrier;

    /**
     * Class constructor.
     */
    public Ferry() {
        // max capacity of ferry
        int maxCapacity = CommandLineArgs.capFerry;
        barrier = new Barrier(maxCapacity /CommandLineArgs.capLorry);
    }

    /** load lorry capacity to ferry */
    public void loadOnFerry(Lorry lorry) {
        if (lorry.getCurrCapacity() == lorry.getMaxCapacity()) barrier.synchronize();
    }
}
