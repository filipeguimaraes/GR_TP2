package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


/**
 * Classe que define o estado do pc num determinado instante.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Estado {
    private final Map<Integer,Process> processos;
    private String uptime;
    private Double cpuTotal;
    private Double ramTotal;

    public Estado() {
        this.uptime = "";
        this.processos = new TreeMap<>();
        this.cpuTotal = 0.0;
        this.ramTotal = 0.0;

    }

    public void addProcess(Process process) {
        this.processos.put(process.getPid(),process);
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public List<Process> getProcessos() {
        return new ArrayList<>(processos.values());
    }

    public void setRamTotal() {
        for (Process p : this.processos.values()) {
            this.ramTotal += p.getMem();
        }
    }

    public void setCpuTotal() {
        for (Process p : this.processos.values()) {
            this.cpuTotal += p.getCpu();
        }
    }

    public Double getCpuTotal() {
        return cpuTotal;
    }

    public Double getRamTotal() {
        return ramTotal;
    }

    public boolean containsPID(Integer PID){
        return this.processos.containsKey(PID);
    }

    public List<Process> getProcessesByName(String name){
        return this.processos.values()
                .stream()
                .filter(process -> process.getName().contains(name))
                .collect(Collectors.toList());
    }

}
