package Model;

import java.util.ArrayList;
import java.util.List;

/*
 *Class que define o estado do pc num determinado instante
 */
public class Estado {
    private String uptime;
    private List<Process> processos;
    private Double cpuTotal;
    private Double ramTotal;

    public Estado() {
        this.uptime = "";
        this.processos = new ArrayList<>();
        this.cpuTotal = 0.0;
        this.ramTotal = 0.0;

    }

    public void addProcess(Process process) {
        this.processos.add(process);
    }

    public String getUptime() {
        return uptime;
    }

    public List<Process> getProcessos() {
        return processos;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public void setRamTotal() {
        for (Process p : this.processos) {
            this.ramTotal += p.getMem();
        }
    }

    public void setCpuTotal() {
        for (Process p : this.processos) {
            this.cpuTotal += p.getCpu();
        }
    }

    public Double getCpuTotal() {
        return cpuTotal;
    }

    public Double getRamTotal() {
        return ramTotal;
    }
}
