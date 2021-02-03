package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Agregador {

    private static Agregador instance = null;
    private final ReentrantLock lock;
    private final List<Estado> estados;
    private final ObservableList<Process> lastProcesses;

    public Agregador() {
        this.estados = new ArrayList<>();
        this.lock = new ReentrantLock();
        lastProcesses = FXCollections.observableArrayList(new ArrayList<>());
    }

    public static Agregador getInstance() {
        if (Agregador.instance == null) {
            Agregador.instance = new Agregador();
        }
        return Agregador.instance;
    }

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


    public List<Estado> getEstados() {
        try {
            lock.lock();
            return estados;
        } finally {
            lock.unlock();
        }
    }

    public ObservableList<Process> getLastProcesses() {
        return lastProcesses;
    }

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

    public String getLastProcessUptime(Integer PID) {
        String uptime = null;
        for (Estado e : this.estados) {
            if (e.containsPID(PID)) {
                uptime = e.getUptime();
            }
        }
        return uptime;
    }

    public long getTotalTimeProcess(Integer PID) throws ParseException {
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss.SS");

        String firstUptime = getFirstProcessUptime(PID);
        Date firstTime = dt.parse(firstUptime);

        String lastUptime = getLastProcessUptime(PID);
        Date lastTime = dt.parse(lastUptime);

        return lastTime.getTime() - firstTime.getTime();
    }

    public Double getLastCPUTotal() {
        return estados.get(estados.size() - 1).getCpuTotal();
    }

    public Double getLastMEMTotal() {
        return estados.get(estados.size() - 1).getRamTotal();
    }

    public List<ProcessGroup> getProcessGroupByName(String name) throws ParseException {
        List<ProcessGroup> pg = new ArrayList<>();
        Map<Integer,List<Process>> aux = new TreeMap<>();

        //Obter todos os processos aglomerados
        for (Estado e : this.estados) {
            for (Process p : e.getProcessesByName(name)){
                if (aux.containsKey(p.getPid())){
                    aux.get(p.getPid()).add(p);
                }else {
                    List<Process> ap = new ArrayList<>();
                    ap.add(p);
                    aux.put(p.getPid(),ap);
                }
            }
        }

        for (List<Process> lista : aux.values()) {
            ProcessGroup tmp = new ProcessGroup(lista.get(0).getPid(),lista.get(0).getName());
            double ramTotal = 0;
            double cpuTotal = 0;
            for (Process e: lista) {
                ramTotal += e.getMem();
                cpuTotal += e.getCpu();
            }
            tmp.setCpu(cpuTotal/lista.size());
            tmp.setMem(ramTotal/lista.size());
            pg.add(tmp);
        }

        for (ProcessGroup p: pg) {
            p.setUptime(getTotalTimeProcess(p.getPid()));
        }
        return pg;
    }

}
