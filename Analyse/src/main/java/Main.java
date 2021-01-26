import Model.Agregador;
import Model.Estado;
import Model.Log;

import java.io.IOException;

public class Main {

    public static void main(final String[] args) throws IOException, InterruptedException {
        Log log = new Log("log.txt");
        log.open();

        new Thread(() -> {
            try {
                log.read(Agregador.getInstance());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(3000);
        MainFX.main(args);
    }
}