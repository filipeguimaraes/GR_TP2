import java.io.IOException;

public class Main {

    public static void main(final String[] args) throws IOException, InterruptedException {
        //MainFX.main(args);
        Log log = new Log("log.txt");
        log.open();
        Agregador agregador = new Agregador();
        log.read(agregador);
    }
}