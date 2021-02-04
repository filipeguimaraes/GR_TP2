package Model;

import javafx.scene.control.TextArea;

import javax.mail.MessagingException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Class para ler do log
 * */

/**
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Log {

    final String path;
    private final TextArea textArea;
    private BufferedReader br;
    private boolean running;
    private boolean doEmail;
    private String email;
    private boolean doCommand;
    private String command;
    private Double cpuThreshold;
    private Double memThreshold;
    private static Log instance;

    public static Log getInstance() throws Exception {
        if (Log.instance == null) {
            throw new Exception("Log does not exist");
        }
        return instance;
    }
    public Log(File file, TextArea textArea) throws IOException {
        this.path = file.getPath();
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

    public void close() throws IOException {
        br.close();
    }


    private void init() throws IOException {
        String line;
        do {
            line = br.readLine();
        } while (line != null);
    }


    public void read() throws IOException, InterruptedException, MessagingException {
        String line = null;
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
                msg += "CPU above threshoold. ";
            }
            if (doEmail) {
                Email email = new Email(this.email);
                email.send("Alarm",msg);
            }
            if (doCommand) {
                ByCommand c = new ByCommand();
                c.send(command,"Alarm",msg);
            }
            printText(msg);
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
        if (textArea == null) {
            System.out.println(text);
        } else {
            textArea.setText(textArea.getText() + text + '\n');
        }
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

