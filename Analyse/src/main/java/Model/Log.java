package Model;

import java.io.*;


/**
 * Ler do ficheiro de log introduzido e carregar os dados para a estrutura.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Log {

    /**
     * Caminho para o ficheiro
     */
    final String path;
    /**
     * Escritor do ficheiro
     */
    private BufferedReader br;
    /**
     * Indica se está a correr ou não.
     */
    private final boolean running;

    /**
     * Construtor de class. Inicializa os objetos.
     *
     * @param file Ficheiro para ler.
     */
    public Log(File file) {
        this.path = file.getPath();
        this.running = true;
    }

    /**
     * Abre o buffer de escrita para o ficheiro.
     *
     * @throws FileNotFoundException Caso não encontre o ficheiro.
     */
    public void open() throws FileNotFoundException {
        br = new BufferedReader(new FileReader(path));
    }


    /**
     * Inicia a leitura do ficheiro.
     * Carrega para o agregador todos os dados que o ficheiro já contem até encontrar o fim.
     *
     * @throws IOException Caso não consiga ler do ficheiro.
     */
    @SuppressWarnings("LoopConditionNotUpdatedInsideLoop")
    public void init() throws IOException{
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
                    if (!first) {
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


    /**
     * Executado a seguir ao #init(). Espera a ter linhas para ler,
     * quando tem carrega os novos dados para o agregador.
     *
     * @throws IOException Quando não consegue ler do ficheiro.
     * @throws InterruptedException Quando a thread é parada inesperadamente.
     */
    @SuppressWarnings("LoopConditionNotUpdatedInsideLoop")
    public void read() throws IOException, InterruptedException {
        String line;
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

