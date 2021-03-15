import java.io.BufferedReader;
import java.io.IOException;
import java.util.TreeSet;

public class Chief implements Runnable{



    /**
     * Pocet nalezenych bloku.
     */
    private int cBlock;

    /**
     * Pocet nalezenych zdroju
     */
    private int cSource;
    /** Vstupni soubor */
    private BufferedReader in;

    /** Pocet delniku */
    private int cWorker;

    /** Doba zpracovani */
    private int tWorker;

    /** Konstruktor tridy Chief.
     * Nacte parametry prikazove radky a zpracuje je.
     * @param args parametry prikazove radky.
     */
    public Chief(String[] args) {
        System.out.println("Predak - start simulace.");
        System.out.println("Predak - inicializace promennych z prikazoveho radku.");
        String inputFile = args[1];
        System.out.println("Input file = \"" + inputFile + "\".");
        String outputFile = args[3];
        System.out.println("Output file = \"" + outputFile + "\".");

        try {
            cWorker = Integer.parseInt(args[5]);
            System.out.println("Pocet delniku: " + cWorker);
        } catch (NumberFormatException e){
            System.err.println("Wrong parameter -cWorker");
            System.exit(1);
        }

        try {
            tWorker = Integer.parseInt(args[7]);
            System.out.println("Delka zpracovani bloku: " + tWorker);
        } catch (NumberFormatException e) {
            System.err.println("Wrong parameter -tWorker");
            System.exit(1);
        }

        try {
            int capLorry = Integer.parseInt(args[9]);
            System.out.println("Kapacita nakladaku: " + capLorry);
        } catch (NumberFormatException e) {
            System.err.println("Wrong parameter -capLorry");
            System.exit(1);
        }
        try {
            int tLorry = Integer.parseInt(args[9]);
            System.out.println("Cas jizdy nakladaku: " + tLorry);
        } catch (NumberFormatException e) {
            System.err.println("Wrong parameter -tLorry");
            System.exit(1);
        }
        try {
            int capFerry = Integer.parseInt(args[13]);
            System.out.println("Kapacita lodi: " + capFerry);
        } catch (NumberFormatException e) {
            System.err.println("Wrong parameter -capFerry");
            System.exit(1);
        }



    }



    @Override
    public void run() {

        System.out.println("Předák - vytváření dělníků.");

    }


    public synchronized String getSource(String jmeno) {

        String jokeText = "";
        String line;

        System.out.println(jmeno + " - Zadam vtip.");

        try {
            while ((line = in.readLine()) != null) {
                if (line.trim().equals("%")) {
                    System.out.println(jmeno + " - Vtip nacten (" + jokeID + ").");
                    jokeID++;
                    return jokeText.trim();
                }
                jokeText += line + "\n";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(jmeno + " - Neni co cist.");
        return "$$skonci$$";
    }
}
