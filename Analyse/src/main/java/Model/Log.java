package Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * Class para ler do log
 * */
public class Log {

    final String path;
    private BufferedReader br;

    public Log(String path) {
        this.path = path;
    }

    public void open() throws FileNotFoundException {
        br = new BufferedReader(new FileReader(path));
    }

    public void close() throws IOException {
        br.close();
    }


    public void read(Agregador agregador) throws IOException, InterruptedException {
        String line = null;
        Estado estado = new Estado();
        while (true) {
            line = br.readLine();
            if (line == null) {
                estado = new Estado();
                Thread.sleep(1000);
            } else {
                if (line.contains("uptime")) {
                    if(!estado.getUptime().isEmpty()){
                        agregador.addEstado(estado);
                        estado = new Estado();
                    }
                    estado.setUptime(Tradutor.linhaToUptime(line));
                } else {
                    estado.addProcess(Tradutor.linhaProcess(line));
                }
            }
        }
    }


}

