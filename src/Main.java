import java.util.Arrays;

public class Main {


    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));

        Chief chief = new Chief(args);


        Thread t = new Thread(chief);
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
