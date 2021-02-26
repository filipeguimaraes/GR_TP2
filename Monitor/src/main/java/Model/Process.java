package Model;

import org.snmp4j.smi.OID;

/**
 * Classe que modela um processo
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Process {
    private final Integer pid;
    private final String name;
    private float mem;
    private float cpu;
    private float centiseconds;

    /**
     * Construtor da classe. Recebe o nome e o OID, que retira a partir deste o PID.
     *
     * @param oid_pid OID do processo
     * @param name Nome do processo
     */
    public Process(OID oid_pid, String name) {
        this.pid = oid_pid.last();
        this.name = name;
    }

    /**
     * Método que determina a percentagem de memória usada por este processo.
     *
     * @param mem Memória gasta pelo processo.
     * @param totalMemory Memória total no sistema.
     */
    public void setMem(Integer mem, Integer totalMemory) {
        float memory = (float) mem;
        float total = (float) totalMemory;
        this.mem = memory / total * 100;
    }

    /**
     * Método que determina a percentagem do cpu. Caso seja a primeira vez coloca a 0.
     *
     * @param old Processo no polling anterior.
     * @param polling Tempo de polling.
     */
    public void setCpu(Process old, int polling) {
        if (old == null) {
            this.cpu = 0;
        } else {
            float oldCentiseconds = old.getCentiseconds();
            this.cpu = (this.centiseconds - oldCentiseconds) / polling;
        }

    }

    /**
     * Retorna os centiceconds deste processo.
     *
     * @return Tempo de CPU.
     */
    public float getCentiseconds() {
        return centiseconds;
    }

    /**
     * Altera o numero de centiseconds deste processo.
     *
     * @param centiseconds Tempo de CPU.
     */
    public void setCentiseconds(float centiseconds) {
        this.centiseconds = centiseconds;
    }

    /**
     * Retorna o PID do processo.
     *
     * @return PID do processo.
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * Alteração do método toString herdado para facilmente imprimir um processo no ficheiro.
     *
     * @return Linha formatada com o processo.
     */
    @Override
    public String toString() {
        return "{ \"process\" = {" +
                "\"pid\":" + pid +
                ", \"name\":\"" + name + '\"' +
                ", \"mem\":" + mem +
                ", \"cpu\":" + cpu +
                "} }";
    }
}
