import javafx.scene.control.TextArea;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Monitorization {

    final int pulling;
    final String pathToLog;
    private final String address;
    private final String port;
    private TextArea textArea;

    public Monitorization(String address, String port, String pathToLog, TextArea textArea) {
        this.textArea = textArea;
        this.pulling = VARIABLES.PULLINGTIME;
        this.pathToLog = pathToLog;
        this.address = address;
        this.port = port;
    }

    public static void main(String[] args)
            throws IOException, InterruptedException {
        Monitorization monitor = new Monitorization(
                "127.0.0.1",
                "161",
                "log.txt",
                null);
        monitor.start();
    }

    public void start() throws IOException, InterruptedException {
        //Log log = new Log(pathToLog);
        //log.append("teste");
        if (address == null || port == null) {
            throw new IOException("Endereço ou porta não indicados!");
        }
        Client client = new Client(this.address + "/" + this.port);
        try {
            println(getUptime(client));
            println(getDate(client).toString());
            String sysDescr = client.getString(VARIABLES.SYSDESCR);
            println(sysDescr);
            Map<Integer, Process> processos = getProcesses(client);

            for (Process p : processos.values()) {
                println(p.toString());
            }
        } finally {
            client.stop();
        }
    }


    public String getUptime(Client client) throws IOException {
        return String.format("{\"uptime\" = \"%s\" }",
                client.getString(VARIABLES.HRSYSTEMUPTIME));
    }

    public LocalDateTime getDate(Client client) throws IOException {
        String datetime = client.getOctecString(VARIABLES.HRSYSTEMDATE);
        println(datetime);
        String formater = "yyyy-MM-dd','hh:mm:ss'.'S','Z";

        return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern(formater));
    }

    public Map<Integer, Process> getProcesses(Client client) throws IOException {
        Map<Integer, Process> processos = client.getName(VARIABLES.HRSWRUNNAME);
        client.getMem(VARIABLES.HRSWRUNPERFMEM, processos);
        client.getCPU(VARIABLES.HRSWRUNPERFCPU, processos);
        return processos;
    }

    public void println(String text) {
        if (textArea == null) {
            System.out.println(text);
        } else textArea.setText(textArea.getText() + text + '\n');
    }

}
