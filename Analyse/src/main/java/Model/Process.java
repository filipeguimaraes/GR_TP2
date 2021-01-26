package Model;

import javax.swing.*;

/*
 * Classe com a estrutura de um processo bem como os métodos a eles aplicados
 */
public class Process {
    private final Integer pid;
    private final String name;
    private final Double mem;
    private final Double cpu;


    public Process(Integer pid, String name, Double mem, Double cpu) {
        this.pid = pid;
        this.name = name;
        this.mem = mem;
        this.cpu = cpu;
    }


    public Integer getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public Double getMem() {
        return mem;
    }

    public Double getCpu() {
        return cpu;
    }
}
