import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Chief implements Runnable {


    private int resIndex;
    private List<Integer> resources;
    /**
     * Pocet nalezenych zdroju
     */
    private int cSource = 0;

    /** Vstupni soubor */
    public BufferedWriter out;


    /** Pocet delniku */
    private int cWorker;

    /** Doba zpracovani */
    private int tWorker;

    private Worker[] workers;

    /** Konstruktor tridy Chief.
     * Nacte parametry prikazove radky a zpracuje je.
     * @param args parametry prikazove radky.
     */
    public Chief(String[] args) {

        try {

            // Output file
            String outputFile = args[3];
            FileWriter fw = new FileWriter(outputFile);
            out = new BufferedWriter(fw);
            out.write("Output file = \"" + outputFile + "\".\n");
            System.out.println("Output file = \"" + outputFile + "\".");

            // Input file
            String inputFile = args[1];
            System.out.println("Input file = \"" + inputFile + "\".");
            out.write("Input file = \"" + inputFile + "\"."+ "\n");
            initResources(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Predak - start simulace."+ "\n");
        try {
            out.write("Predak - start simulace.\nPredak - inicializace promennych z prikazoveho radku."+ "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Predak - inicializace promennych z prikazoveho radku.");


        try {
            this.cWorker = Integer.parseInt(args[5]);
            System.out.println("Pocet delniku: " + cWorker);
            out.write("Pocet delniku: " + cWorker + "\n");
            workers = new Worker[this.cWorker];
        } catch (NumberFormatException | IOException e){
            System.err.println("Wrong parameter -cWorker");
            System.exit(1);
        }

        try {
            tWorker = Integer.parseInt(args[7]);
            System.out.println("Delka zpracovani bloku: " + tWorker);
            out.write("Delka zpracovani bloku: " + tWorker + "\n");
        } catch (NumberFormatException | IOException e) {
            System.err.println("Wrong parameter -tWorker");
            System.exit(1);
        }

        try {
            int capLorry = Integer.parseInt(args[9]);
            System.out.println("Kapacita nakladaku: " + capLorry);
            out.write("Kapacita nakladaku: " + capLorry + "\n");
        } catch (NumberFormatException | IOException e) {
            System.err.println("Wrong parameter -capLorry");
            System.exit(1);
        }
        try {
            int tLorry = Integer.parseInt(args[9]);
            System.out.println("Cas jizdy nakladaku: " + tLorry + "\n");
            out.write("Cas jizdy nakladaku: " + tLorry);
        } catch (NumberFormatException | IOException e) {
            System.err.println("Wrong parameter -tLorry");
            System.exit(1);
        }
        try {
            int capFerry = Integer.parseInt(args[13]);
            System.out.println("Kapacita lodi: " + capFerry);
            out.write("Kapacita lodi: " + capFerry + "\n");
        } catch (NumberFormatException | IOException e) {
            System.err.println("Wrong parameter -capFerry");
            System.exit(1);
        }



    }



    @Override
    public void run() {

        try {
            out.write("Předák - vytváření dělníků.\n");

            System.out.println("Předák - vytváření dělníků.");

            for (int i  = 0; i < cWorker; i++) {
                //workers[i] = new Worker("Delnik" + (i+1), this, tWorker);

            }
            out.close();
          //  in.close();
        }catch (IOException e){
            System.err.println("Problem s buffered writerem.");
        }
    }

    /**
     * Ziskani zdroje pro kazdeho delnika.
     * @param jmeno jmeno delnika
     * @return vrati pocet bloku
     */
    public synchronized int getSources(String jmeno) { // TODO vymyslet vhodny nazev




        System.out.println(jmeno + " - neni uz co tezit");
        return -1;
    }

    private void initResources(String inputFile) {
        resources = new ArrayList<>();
        int blocksCount = 0;
        FileReader fr = null;
        try {
            fr = new FileReader(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader in = new BufferedReader(fr);

        int ch;
        try {
            while ((ch = in.read()) != -1 || blocksCount > 0) {
                if (ch == 'x') {
                    blocksCount++;
                    continue;
                }
                if (blocksCount > 0) {
                    resources.add(resIndex, blocksCount);
                    resIndex++;
                    blocksCount = 0;
                }
            }
            System.out.println(resources.toString());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
