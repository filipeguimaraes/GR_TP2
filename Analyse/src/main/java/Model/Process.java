package Model;


/**
 * Classe com a estrutura de um processo bem como os métodos a eles aplicados
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Process {
    /**
     *
     */
    private final Integer pid;
    /**
     *
     */
    private final String name;
    /**
     *
     */
    private final Double mem;
    /**
     *
     */
    private final Double cpu;

    /**
     * @param pid
     * @param name
     * @param mem
     * @param cpu
     */
    public Process(Integer pid, String name, Double mem, Double cpu) {
        this.pid = pid;
        this.name = name;
        this.mem = mem;
        this.cpu = cpu;
    }

    /**
     * @return
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public Double getMem() {
        return mem;
    }

    /**
     * @return
     */
    public Double getCpu() {
        return cpu;
    }
}
