package Model;

import java.io.*;

/*
 * Class para ler do log
 * */
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
        Estado estado = new Estado();
        boolean first = true;
        while (running) {
            line = br.readLine();
            if (line == null) {
                Agregador.getInstance().addEstado(estado);
                break;
            } else {
                if (line.contains("uptime")) {
                    if (!first){
                        Agregador.getInstance().addEstado(estado);
                    }
                    estado = new Estado();
                    estado.setUptime(Tradutor.linhaToUptime(line));
                    first = false;
                } else {
                    estado.addProcess(Tradutor.linhaProcess(line));
                }
            }
        }
    }


    public void read() throws IOException, InterruptedException {
        String line = null;
        Estado estado = new Estado();
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

