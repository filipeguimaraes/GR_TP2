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
    /**
     * Mapa que armazena todos os processos do estado. A chave é o PID.
     */
    private final Map<Integer, Process> processos;
    /**
     * Uptime da maquina neste estado.
     */
    private String uptime;
    /**
     * Percentagem de CPU total neste estado.
     */
    private Double cpuTotal;
    /**
     * Percentagem de memória total neste estado.
     */
    private Double ramTotal;

    /**
     * Construtor da classe. Inicia todos os objetos.
     */
    public Estado() {
        this.uptime = "";
        this.processos = new TreeMap<>();
        this.cpuTotal = 0.0;
        this.ramTotal = 0.0;

    }

    /**
     * Adiciona o processo ao mapa colocando a respetiva chave.
     *
     * @param process Processo a adicionar.
     */
    public void addProcess(Process process) {
        this.processos.put(process.getPid(), process);
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    /**
     * Obter a lista dos processos neste estado a partir do mapa.
     *
     * @return Lista de processos.
     */
    public List<Process> getProcessos() {
        return new ArrayList<>(processos.values());
    }

    /**
     * Calcula a memoria total ocupada pelos processos.
     */
    public void setRamTotal() {
        for (Process p : this.processos.values()) {
            this.ramTotal += p.getMem();
        }
    }

    /**
     * Calcula a percentagem de cpu total ocupada pelos processos.
     */
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

    /**
     * Verifica se existe um processo com o PID fornecido.
     *
     * @param PID PID do processo.
     * @return Se há algum processo com o PID fornecido.
     */
    public boolean containsPID(Integer PID) {
        return this.processos.containsKey(PID);
    }

    /**
     * Pesquisa dos processos pelo nome.
     *
     * @param name Nome do processo.
     * @return Lista dos processos com o nome fornecido.
     */
    public List<Process> getProcessesByName(String name) {
        return this.processos.values()
                .stream()
                .filter(process -> process.getName().contains(name))
                .collect(Collectors.toList());
    }

}
