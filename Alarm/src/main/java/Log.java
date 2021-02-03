import java.io.*;

/*
 * Class para ler do log
 * */

/**
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Log {

    final String path;
    private BufferedReader br;
    private boolean running;

    public Log(File file) {
        this.path = file.getPath();
        this.running = true;
    }

    public void open() throws FileNotFoundException {
        br = new BufferedReader(new FileReader(path));
    }

    public void close() throws IOException {
        br.close();
    }


    public void init() throws IOException, InterruptedException {
        String line;
        do {
            line = br.readLine();
        } while (line != null);
    }


    public void read() throws IOException, InterruptedException {
        String line = null;
        boolean adicionado = true;
        while (running) {
            line = br.readLine();
            if (line == null) {
                if (!adicionado) {
                    Agregador.getInstance().addEstado(estado);
                    adicionado = true;
                }
                Thread.sleep(1000);
            } else {
                if (line.contains("uptime")) {
                    estado = new Estado();
                    adicionado = false;
                    estado.setUptime(Tradutor.linhaToUptime(line));
                } else {
                    estado.addProcess(Tradutor.linhaProcess(line));
                }
            }
        }
    }


}

