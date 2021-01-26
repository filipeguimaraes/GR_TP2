package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Agregador {

    private static Agregador instance = null;
    private final ReentrantLock lock;
    private List<Estado> estados;
    private long refreshRate;

    public Agregador() {
        this.estados = new ArrayList<>();
        this.lock = new ReentrantLock();
        this.refreshRate = 30;
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
    }

    public Estado getLastEstado() {
        try {
            this.lock.lock();
            return estados.get(estados.size() - 1);
        } finally {
            this.lock.unlock();
        }
    }

    public List<Estado> getEstados() {
        try {
            lock.lock();
            return estados;
        } finally {
            lock.unlock();
        }
    }

    public long getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }
}
