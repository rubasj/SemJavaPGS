public class Ferry {

    private int maxCapacity;
    private int currCapacity;

    public Ferry() {
        this.maxCapacity = CommandLineArgs.capFerry;
        this.currCapacity = 0;
    }

    public void loadOnLorry(Lorry lorry) {
        if (lorry.getCurrCapacity() == lorry.getCurrCapacity()) barrier.synchronize();
    }
}
