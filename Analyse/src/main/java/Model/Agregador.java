package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

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


    public Double getLastCPUTotal() {
        return estados.get(estados.size()-1).getCpuTotal();
    }

    public Double getLastMEMTotal() {
        return estados.get(estados.size()-1).getRamTotal();
    }
}
