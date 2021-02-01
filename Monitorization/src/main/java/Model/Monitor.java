package Model;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Contém todos os métodos diretamente usados pela aplicação.
 * Só poderá existir uma instância desta class no decorrer da execução
 * por isso é chamado o método getInstance de maneira a obter a mesma.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Monitor {

    /**
     * Instância privada partilhada por todos
     */
    private static Monitor instance = null;
    /**
     * Mapa com todas as threads em execução. A chave é composta pelo endereço e porta (address+":"+port)
     */
    private final Map<String, Thread> threads;
    /**
     * Variável responsável por para a execução dos ciclos em todas as threads
     */
    private final boolean running;
    /**
     * Tempo em que a aplicação faz polling dos dados
     */
    private int polling;

    // **************************************************
    // Métodos Privados
    // **************************************************

    private Monitor() {
        this.threads = new HashMap<>();
        this.polling = 30;
        this.running = true;
    }

    // **************************************************
    // Métodos Públicos
    // **************************************************

    /**
     * Método usado em qualquer class para obter a mesma instância seja em que momento for.
     *
     * @return Instância partilhada da class Model.Monitor
     */
    public static Monitor getInstance() {
        if (Monitor.instance == null) {
            Monitor.instance = new Monitor();
        }
        return Monitor.instance;
    }

    /**
     * Método que altera o tempo que o programa faz polling de dados.
     * Este método pode ser chamado em qualquer momento e terá efeito na próxima poll.
     *
     * @param time Tempo de polling
     */
    public void changePulling(int time) {
        this.polling = time;
    }

    /**
     * Método responsável pela criação e preenchimento do ficheiro de log usando uma
     * thread por cada vez que se executa este.
     * Pode ser usado quantas vezes for necessário por cada endereço a monitorizar.
     *
     * @param address   Endereço do sistema a monitorizar
     * @param port      Porta onde será feita a comunicação SNMP
     * @param community String da community
     * @param textArea  Opcional (pode ser null). Objeto que permite imprimir texto na aplicação.
     * @throws IOException Quando não é indicado um endereço ou uma porta válida.
     */
    public void start(String address, String port, String community, TextArea textArea) throws IOException {
        if (address == null || port == null) {
            throw new IOException("Endereço ou porta não indicados!");
        }

        if (this.threads.containsKey(address+":"+port))
            throw new IOException("Endereço já está a ser monitorizado.");

        Log log = new Log(address, port);

        Thread thread = new Thread(() -> {
            Map<Integer, Process> processosAntigos = null;
            while (running) {
                try {
                    Client client = new Client(address + "/" + port, community);

                    Map<Integer, Process> processos = getProcesses(client);
                    if (processosAntigos != null) {
                        log.open();
                        log.append(getUptime(client));
                        for (Process p : processos.values()) {
                            p.setCpu(processosAntigos.get(p.getPid()), this.polling);
                            log.append(p.toString());
                        }
                        log.close();
                        client.stop();
                        textArea.setText(textArea.getText() + address + '\\' + port + ":Escrita no log\n");
                    }
                    processosAntigos = processos;

                    Thread.sleep(polling * 1000L);
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                } catch (InterruptedException e) {
                    printText("Stoping monotoring Address: " + address + "/" + port + "...", textArea);
                    try {
                        Thread.currentThread().join();
                    } catch (InterruptedException interruptedException) {
                        printText("Stop monotoring Address: " + address + "/" + port + "...", textArea);
                    }
                }
            }
        });
        threads.put(address + ":" + port, thread);
        thread.start();

    }

    /**
     * Método para terminar a monitorização de um certo sistema.
     * Faz uso do método interrupt para interromper uma determinada
     * thread do mapa.
     *
     * @param address Endereço do sistema que se está a monitorizar.
     * @param port    Porta do sistema que se está a monitorizar.
     * @param text    Opcional. Objeto para imprimir na aplicação
     */
    public void join(String address, String port, TextArea text) {
        if (this.threads.containsKey(address + ":" + port)) {
            this.threads.get(address + ":" + port).interrupt();
            this.threads.remove(address + ":" + port);
        } else {
            printText("Não foi encontrado esse endereço/porta no sistema.", text);
        }

    }

    /**
     * Método que faz uso da cass Client para solicitar o uptime de um determinado sistema.
     *
     * @param client Instância Model.Client de qual sistema estamos a monitorizar.
     * @return Há quanto tempo que o pc está ligado.
     * @throws IOException Exceção quando não se consegue obter o uptime.
     */
    public String getUptime(Client client) throws IOException {
        return String.format("{ \"uptime\" = \"%s\" }",
                client.getString(VARIABLES.HRSYSTEMUPTIME));
    }

    /**
     * Método para obter os processos a correr no sistema.
     *
     * @param client Instância Client de qual sistema estamos a monitorizar
     * @return Um mapa com todos os processos que tem como chave o PID dos mesmos.
     * @throws IOException Se não conseguir obter o pretendido
     */
    public Map<Integer, Process> getProcesses(Client client) throws IOException {
        Integer totalMemory = Integer.parseInt(client.getString(VARIABLES.HRMEMORYSIZE));
        Map<Integer, Process> processos = client.getName(VARIABLES.HRSWRUNNAME);
        client.getMem(VARIABLES.HRSWRUNPERFMEM, processos, totalMemory);
        client.getCPU(VARIABLES.HRSWRUNPERFCPU, processos);
        return processos;
    }

    /**
     * Método para imprimir texto no System.out ou, quando existir, na aplicação.
     *
     * @param text     Texto a imprimir
     * @param textArea Opcional. Se existir o texto é imprimido para este objeto
     */
    public void printText(String text, TextArea textArea) {
        if (textArea == null) {
            System.out.println(text);
        } else {
            textArea.setText(textArea.getText() + text + '\n');
        }
    }

    /**
     * Método que verifica se ainda há threads a correr no sistema.
     *
     * @return Retorna true caso haja threads, false caso contrário.
     */
    public boolean haveThreads() {
        return !this.threads.isEmpty();
    }
}
