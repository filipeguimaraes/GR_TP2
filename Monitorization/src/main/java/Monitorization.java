import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.Map;

public class Monitorization {

    private static Monitorization instance = null;
    private Thread thread;
    private final String address;
    private final String port;
    private int pulling;
    private TextArea textArea;
    private boolean running = true;

    public Monitorization(String address, String port, TextArea textArea) {
        this.textArea = textArea;
        this.pulling = VARIABLES.PULLINGTIME;
        this.address = address;
        this.port = port;
    }

    public void start() throws IOException, InterruptedException {
        Log log = new Log();

        if (address == null || port == null) {
            throw new IOException("Endereço ou porta não indicados!");
        }
        thread = new Thread(() -> {
            while (running) {
                try {
                    Client client = new Client(this.address + "/" + this.port);
                    log.open();
                    log.append(getUptime(client));
                    String sysDescr = String.format("{ \"sysINFO\" = \"%s\" }", client.getString(VARIABLES.SYSDESCR));
                    log.append(sysDescr);
                    Map<Integer, Process> processos = getProcesses(client);

                    for (Process p : processos.values()) {
                        log.append(p.toString());
                    }
                    log.close();
                    client.stop();
                    textArea.setText(textArea.getText() +this.address+'\\'+this.port+":Escrita no log\n");
                    Thread.sleep(pulling * 1000L);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });
        thread.start();

    }

    public void stop(){
        running = false;
    }

    public String getUptime(Client client) throws IOException {
        return String.format("{ \"uptime\" = \"%s\" }",
                client.getString(VARIABLES.HRSYSTEMUPTIME));
    }


    public Map<Integer, Process> getProcesses(Client client) throws IOException {
        Map<Integer, Process> processos = client.getName(VARIABLES.HRSWRUNNAME);
        client.getMem(VARIABLES.HRSWRUNPERFMEM, processos);
        client.getCPU(VARIABLES.HRSWRUNPERFCPU, processos);
        return processos;
    }


}
