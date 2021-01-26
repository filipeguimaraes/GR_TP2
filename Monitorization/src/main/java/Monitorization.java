import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Monitorization {

    private static Monitorization instance = null;

    //(address+":"+port,thread)
    private Map<String, Thread> threads;
    private int polling;
    private boolean running = true;

    private Monitorization() {
        this.threads = new HashMap<>();
        this.polling = 30;
    }

    public static Monitorization getInstance() {
        if (Monitorization.instance == null) {
            Monitorization.instance = new Monitorization();
        }
        return Monitorization.instance;
    }

    public void changePulling(int time) {
        this.polling = time;
    }

    public void start(String address, String port, String community, TextArea textArea) throws IOException {
        if (address == null || port == null) {
            throw new IOException("Endereço ou porta não indicados!");
        }
        Log log = new Log(address, port);

        Thread thread = new Thread(() -> {
            Map<Integer, Process> processosAntigos = null;
            while (running) {
                try {
                    Client client = new Client(address + "/" + port, community);
                    log.open();

                    //String sysDescr = String.format("{ \"sysINFO\" = \"%s\" }", client.getString(VARIABLES.SYSDESCR));
                    //log.append(sysDescr);
                    Map<Integer, Process> processos = getProcesses(client);
                    if (processosAntigos != null) {
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


    public void join(String address, String port, TextArea textArea) {
        this.threads.get(address + ":" + port).interrupt();

    }

    public void stop() {
        running = false;
    }

    public String getUptime(Client client) throws IOException {
        return String.format("{ \"uptime\" = \"%s\" }",
                client.getString(VARIABLES.HRSYSTEMUPTIME));
    }


    public Map<Integer, Process> getProcesses(Client client) throws IOException {
        Integer totalMemory = Integer.parseInt(client.getString(VARIABLES.HRMEMORYSIZE));
        Map<Integer, Process> processos = client.getName(VARIABLES.HRSWRUNNAME);
        client.getMem(VARIABLES.HRSWRUNPERFMEM, processos, totalMemory);
        client.getCPU(VARIABLES.HRSWRUNPERFCPU, processos);
        return processos;
    }

    public void printText(String text, TextArea textArea) {
        textArea.setText(textArea.getText() + text + '\n');
    }


}
