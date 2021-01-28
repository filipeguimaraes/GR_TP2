package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Agregador {

    private static Agregador instance = null;
    private final ReentrantLock lock;
    private List<Estado> estados;
    private Estado lastEstado;
    private ObservableList<Process> lastProcesses;

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
        System.out.println(estado.getUptime());
        lastEstado = estado;
        lastProcesses.removeAll();
        lastProcesses.addAll(FXCollections.observableArrayList(lastEstado.getProcessos()));
    }

    public Estado getLastEstado() {
        return this.lastEstado;
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
}
