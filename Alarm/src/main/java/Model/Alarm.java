package Model;

import javafx.scene.control.TextArea;

import javax.mail.MessagingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/*
 * Class para ler do log
 * */

/**
 * Classe responsável por ler o ficheiro de log's e gerar alarmes.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Alarm {
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
     *
     */
    private BufferedReader br;
    /**
     *
     */
    private final boolean running;
    /**
     *
     */
    private boolean doEmail;
    /**
     *
     */
    private String email;
    /**
     *
     */
    private boolean doCommand;
    /**
     *
     */
    private String command;
    /**
     *
     */
    private Double cpuThreshold;
    /**
     *
     */
    private Double memThreshold;
    /**
     *
     */
    private static Alarm instance;

    /**
     *  Obter a instância global desta classe.
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
     * Construtor da aplicação.
     * Não deve ser chamado mais que uma vez na execução da aplicação.
     *
     * @param file
     * @param textArea
     * @throws IOException
     */
    public Alarm(File file, TextArea textArea) throws IOException {
        this.path = file.getPath();
        String alarmPath = path.replace("log","alarm");
        this.alarmFile = new File(alarmPath);
        if(!alarmFile.exists()){
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

    private void open() throws FileNotFoundException {
        br = new BufferedReader(new FileReader(path));
    }


    private void init() throws IOException {
        String line;
        do {
            line = br.readLine();
        } while (line != null);
    }


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

    public void verifyProcesses(List<Process> processes) throws MessagingException, IOException {
        boolean mem = verifyMEM(processes);
        boolean cpu = verifyCPU(processes);
        if (mem || cpu) {
            String msg = "Alarm activated: ";
            if (mem) {
                msg += "Memory above threshold. ";
            }
            if (cpu){
                msg += "CPU above threshold. ";
            }
            if (doEmail) {
                Email email = new Email(this.email);
                email.send("Alarm",msg);
            }
            if (doCommand) {
                sendByCommand("Alarm",msg);
            }
            printText(msg);
            appendToAlarmFile(msg);
        }else printText("No alarms were generated.");
    }

    private boolean verifyCPU(List<Process> processes) {
        Double totalCpu = 0.0;
        for (Process p : processes) {
            totalCpu += p.getCpu();
        }
        return totalCpu >= this.cpuThreshold;
    }

    private boolean verifyMEM(List<Process> processes) {
        Double totalMem = 0.0;
        for (Process p : processes) {
            totalMem += p.getMem();
        }
        return totalMem >= this.memThreshold;
    }

    /**
     * Método para imprimir texto no System.out ou, quando existir, na aplicação.
     *
     * @param text Texto a imprimir
     */
    public void printText(String text) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        text = "["+ LocalTime.now().format(dtf)+"] "+text;
        if (textArea == null) {
            System.out.println(text);
        } else {
            textArea.setText(textArea.getText() + text + '\n');
        }
    }


    public void appendToAlarmFile(String text) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        text = "["+ LocalTime.now().format(dtf)+"] "+text;
        DataOutputStream dataOut = new DataOutputStream(new FileOutputStream(alarmFile, true));
        dataOut.write((text + '\n').getBytes(StandardCharsets.UTF_8));
        dataOut.flush();
        dataOut.close();
    }

    public void sendByCommand(String subject, String msg) throws IOException {
        String param[] = { command, subject, msg };
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

