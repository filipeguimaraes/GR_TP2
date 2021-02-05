package Model;

import javafx.scene.control.TextArea;

import javax.mail.MessagingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por ler o ficheiro de log's e gerar alarmes.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Alarm {
    /**
     * Instância única da classe
     */
    private static Alarm instance;
    /**
     * Caminho para o ficheiro de log's
     */
    private final String path;
    /**
     * Caminho para o ficheiro de alarmes
     */
    private final File alarmFile;
    /**
     * Area para escrever na aplicação
     */
    private final TextArea textArea;
    /**
     * Estado de funcionamento da leitura
     */
    private final boolean running;
    /**
     * Buffer de leituda do ficheiro de log's
     */
    private BufferedReader br;
    /**
     * Controla se envia ou não email
     */
    private boolean doEmail;
    /**
     * Email para enviar os alarmes
     */
    private String email;
    /**
     * Controla se envia ou não notificação por comando.
     */
    private boolean doCommand;
    /**
     * Comando a ser usado para enviar alarmes.
     */
    private String command;
    /**
     * Threshold de percentagem de cpu
     */
    private Double cpuThreshold;
    /**
     * Threshold de percentagem de memória
     */
    private Double memThreshold;

    /**
     * Construtor da aplicação.
     * Não deve ser chamado mais que uma vez na execução da aplicação.
     *
     * @param file     Ficheiro de log.
     * @param textArea Area para escrever.
     * @throws IOException Caso não consiga abrir o ficheiro.
     */
    public Alarm(File file, TextArea textArea) throws IOException {
        this.path = file.getPath();
        String alarmPath = path.replace("log", "alarm");
        this.alarmFile = new File(alarmPath);
        if (!alarmFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            alarmFile.createNewFile();
        }
        this.running = true;
        this.textArea = textArea;
        this.doEmail = false;
        this.doCommand = false;
        this.cpuThreshold = 200.0;
        this.memThreshold = 95.0;
        instance = this;
        open();
        init();
    }

    /**
     * Obter a instância global desta classe.
     *
     * @return Instência da classe.
     * @throws Exception Coso não exista uma instância.
     */
    public static Alarm getInstance() throws Exception {
        if (Alarm.instance == null) {
            throw new Exception("Alarm does not exist");
        }
        return instance;
    }

    /**
     * Abrir o buffer de leitura do ficheiro de log.
     *
     * @throws FileNotFoundException Caso não encontre o ficheiro.
     */
    private void open() throws FileNotFoundException {
        br = new BufferedReader(new FileReader(path));
    }

    /**
     * Usado para chegar ao fim do ficheiro.
     *
     * @throws IOException Caso não consiga ler do ficheiro.
     */
    private void init() throws IOException {
        String line;
        do {
            line = br.readLine();
        } while (line != null);
    }

    /**
     * Responsável por ler do ficheiro log e processar se é preciso ou não disparar o alarme.
     *
     * @throws IOException          Caso não consiga ler.
     * @throws InterruptedException Se a thread parar inesperadamente.
     * @throws MessagingException   Caso não consiga enviar um email.
     */
    @SuppressWarnings("LoopConditionNotUpdatedInsideLoop")
    public void read() throws IOException, InterruptedException, MessagingException {
        String line;
        List<Process> processes = new ArrayList<>();
        boolean adicionado = true;
        while (running) {
            line = br.readLine();
            if (line == null) {
                if (!adicionado) {
                    verifyProcesses(processes);
                    adicionado = true;
                    processes = new ArrayList<>();
                }
                Thread.sleep(1000);
            } else {
                if (line.contains("uptime")) {
                    adicionado = false;
                } else {
                    processes.add(Tradutor.linhaProcess(line));
                }
            }
        }
    }

    /**
     * Verifica se com os processos fornecidos é ultrapassado algum threshold.
     * Caso seja envia o alarme.
     *
     * @param processes Processos a analisar
     * @throws MessagingException Caso não consiga enviar o email.
     * @throws IOException        Caso não consiga escrever no ficheiro de alarme.
     */
    public void verifyProcesses(List<Process> processes) throws MessagingException, IOException {
        boolean mem = verifyMEM(processes);
        boolean cpu = verifyCPU(processes);
        if (mem || cpu) {
            String msg = "Alarm activated: ";
            if (mem) {
                msg += "Memory above threshold. ";
            }
            if (cpu) {
                msg += "CPU above threshold. ";
            }
            if (doEmail) {
                Email email = new Email(this.email);
                email.send("Alarm", msg);
            }
            if (doCommand) {
                sendByCommand("Alarm", msg);
            }
            printText(msg);
            appendToAlarmFile(msg);
        } else printText("No alarms were generated.");
    }

    /**
     * Verifica se o cpu ultrapassa o threshold.
     *
     * @param processes Lista de processos a analisar.
     * @return True caso ultrapasse, false caso contrário.
     */
    private boolean verifyCPU(List<Process> processes) {
        Double totalCpu = 0.0;
        for (Process p : processes) {
            totalCpu += p.getCpu();
        }
        return totalCpu >= this.cpuThreshold;
    }

    /**
     * Verifica se a memória ultrapassa o threshold.
     *
     * @param processes Lista de processos a analisar.
     * @return True caso ultrapasse, false caso contrário.
     */
    private boolean verifyMEM(List<Process> processes) {
        Double totalMem = 0.0;
        for (Process p : processes) {
            totalMem += p.getMem();
        }
        return totalMem >= this.memThreshold;
    }

    /**
     * Método para imprimir texto no System.out ou, quando existir, na aplicação.
     * Imprime já com a hora atual para mais fácil análise.
     *
     * @param text Texto a imprimir
     */
    public void printText(String text) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        text = "[" + LocalTime.now().format(dtf) + "] " + text;
        if (textArea == null) {
            System.out.println(text);
        } else {
            textArea.setText(textArea.getText() + text + '\n');
        }
    }


    /**
     * Escreve os alarmes para o ficheiro de alarmes.
     *
     * @param text Alarme a escrever.
     * @throws IOException Caso não consiga escrever no ficheiro.
     */
    public void appendToAlarmFile(String text) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        text = "[" + LocalTime.now().format(dtf) + "] " + text;
        DataOutputStream dataOut = new DataOutputStream(new FileOutputStream(alarmFile, true));
        dataOut.write((text + '\n').getBytes(StandardCharsets.UTF_8));
        dataOut.flush();
        dataOut.close();
    }

    /**
     * Usado para executar o commando que gera alarmes no sistema operativo.
     *
     * @param subject Título do alarme.
     * @param msg     Mensagem do alarme
     * @throws IOException Caso não consiga executar o comando.
     */
    public void sendByCommand(String subject, String msg) throws IOException {
        String param[] = {command, subject, msg};
        Runtime.getRuntime().exec(param);
    }

    public void setDoEmail(boolean doEmail) {
        this.doEmail = doEmail;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDoCommand(boolean doCommand) {
        this.doCommand = doCommand;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setCpuThreshold(Double cpuThreshold) {
        this.cpuThreshold = cpuThreshold;
    }

    public void setMemThreshold(Double memThreshold) {
        this.memThreshold = memThreshold;
    }
}

