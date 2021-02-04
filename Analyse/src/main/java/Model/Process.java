package Model;


/**
 * Classe com a estrutura de um processo.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Process {
    private final Integer pid;
    private final String name;
    private final Double mem;
    private final Double cpu;

    /**
     * Construtor de classe.
     *
     * @param pid PID do processo.
     * @param name Nome do processo.
     * @param mem Percentagem de memória.
     * @param cpu Percentagem de CPU.
     */
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
