package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Classe que agrega todos os dados de estados recolhidos.
 * Só existe uma instância desta classe na aplicação.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Agregador {
    /**
     * Instância única para esta classe
     */
    private static Agregador instance = null;
    /**
     * Variável para gestão de concorrência a ler e escrever para a estrutura de dados.
     */
    private final ReentrantLock lock;
    /**
     * Lista com todos os estados do programa.
     */
    private final List<Estado> estados;
    /**
     * Lista auxiliar para apresentar na tabela e atualizar automaticamente.
     */
    private final ObservableList<Process> lastProcesses;

    // **************************************************
    // Métodos Privados
    // **************************************************

    private Agregador() {
        this.estados = new ArrayList<>();
        this.lock = new ReentrantLock();
        lastProcesses = FXCollections.observableArrayList(new ArrayList<>());
    }

    // **************************************************
    // Métodos Públicos
    // **************************************************

    /**
     * Obter a instância única da classe.
     *
     * @return Sempre a mesma instância da classe.
     */
    public static Agregador getInstance() {
        if (Agregador.instance == null) {
            Agregador.instance = new Agregador();
        }
        return Agregador.instance;
    }

    /**
     * Adicionar um estado ao Agregador.
     * Atualiza também os últimos processos a correr.
     *
     * @param estado Estado a adicionar.
     */
    public void addEstado(Estado estado) {
        estado.setCpuTotal();
        estado.setRamTotal();
        try {
            this.lock.lock();
            this.estados.add(estado);
        } finally {
            this.lock.unlock();
        }

        lastProcesses.clear();
        lastProcesses.addAll(FXCollections.observableArrayList(estado.getProcessos()));
    }

    /**
     * Obter a lista de estados no agregador.
     *
     * @return Lista de estados no agregador.
     */
    public List<Estado> getEstados() {
        try {
            lock.lock();
            return estados;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Obter a lista dos últimos processos.
     *
     * @return Lista com os últimos processos a correr no sistema.
     */
    public ObservableList<Process> getLastProcesses() {
        return lastProcesses;
    }

    /**
     * Encontrar o primeiro uptime em que o processo aparece.
     *
     * @param PID PID do processo.
     * @return Primeiro uptime do processo.
     */
    public String getFirstProcessUptime(Integer PID) {
        String uptime = null;
        for (Estado e : this.estados) {
            if (e.containsPID(PID)) {
                uptime = e.getUptime();
                break;
            }
        }
        return uptime;
    }

    /**
     * Encontrar o último uptime em que o processo aparece.
     *
     * @param PID PID do processo.
     * @return Último uptime do processo.
     */
    public String getLastProcessUptime(Integer PID) {
        String uptime = null;
        for (Estado e : this.estados) {
            if (e.containsPID(PID)) {
                uptime = e.getUptime();
            }
        }
        return uptime;
    }

    /**
     * A partir do primeiro e do último uptime calcula o tempo total em que o processo está/esteve ativo.
     *
     * @param PID PID do processo
     * @return Total de tempo em que o processo esteve, está ativo.
     * @throws ParseException Caso não consiga fazer parse ao uptime.
     */
    public long getTotalTimeProcess(Integer PID) throws ParseException {
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss.SS");

        String firstUptime = getFirstProcessUptime(PID);
        Date firstTime = dt.parse(firstUptime);

        String lastUptime = getLastProcessUptime(PID);
        Date lastTime = dt.parse(lastUptime);

        return lastTime.getTime() - firstTime.getTime();
    }

    /**
     * Calcular o valor total de cpu no ultimo estado.
     *
     * @return Valor total em percentagem de cpu no ultimo estado.
     */
    public Double getLastCPUTotal() {
        return estados.get(estados.size() - 1).getCpuTotal();
    }

    /**
     * Calcular o valor total de memória no ultimo estado.
     *
     * @return Valor total em percentagem de memória no ultimo estado.
     */
    public Double getLastMEMTotal() {
        return estados.get(estados.size() - 1).getRamTotal();
    }

    /**
     * Obter todos os processos ( por nome, ou não ) que estão ou estiveram a correr na máquina.
     * Aglomera dados destes processos como a média de cpu e memória ocupados ao longo do tempo,
     * bem como o tempo que esteve ativo.
     *
     * @param name Nome que o processo contém. Vazio para apresentar todos.
     * @return Lista com os processos e os respetivos dados.
     * @throws ParseException Caso não consiga calcular o tempo que esteve ativo.
     */
    public List<ProcessGroup> getProcessGroupByName(String name) throws ParseException {
        List<ProcessGroup> pg = new ArrayList<>();
        Map<Integer, List<Process>> aux = new TreeMap<>();

        //Obter todos os processos aglomerados por PID num mapa
        for (Estado e : this.estados) {
            for (Process p : e.getProcessesByName(name)) {
                if (aux.containsKey(p.getPid())) {
                    aux.get(p.getPid()).add(p);
                } else {
                    List<Process> ap = new ArrayList<>();
                    ap.add(p);
                    aux.put(p.getPid(), ap);
                }
            }
        }

        //Calcular mem e cpu total por processo encontrado
        for (List<Process> lista : aux.values()) {
            ProcessGroup tmp = new ProcessGroup(lista.get(0).getPid(), lista.get(0).getName());
            double ramTotal = 0;
            double cpuTotal = 0;
            for (Process e : lista) {
                ramTotal += e.getMem();
                cpuTotal += e.getCpu();
            }
            tmp.setCpu(cpuTotal / lista.size());
            tmp.setMem(ramTotal / lista.size());
            pg.add(tmp);
        }

        //calcular o tempo ativo
        for (ProcessGroup p : pg) {
            p.setUptime(getTotalTimeProcess(p.getPid()));
        }
        return pg;
    }

}
