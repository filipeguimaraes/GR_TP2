package Model;

/**
 * Estrutura de um processo para apresentar a média de cpu e memória
 * ao longo do tempo e a respetiva duração.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class ProcessGroup {
    /**
     * PID do processo.
     */
    private Integer pid;
    /**
     * Nome do processo.
     */
    private String name;
    /**
     * Percentagem média de cpu.
     */
    private Double cpu;
    /**
     * Percentagem média de memória.
     */
    private Double mem;
    /**
     * Tempo ativo.
     */
    private Long uptime;

    /**
     * Construtor de um processo.
     *
     * @param pid  PID do processo.
     * @param name Nome do processo.
     */
    public ProcessGroup(Integer pid, String name) {
        this.pid = pid;
        this.name = name;
    }


    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCpu() {
        return cpu;
    }

    public void setCpu(Double cpu) {
        this.cpu = cpu;
    }

    public Double getMem() {
        return mem;
    }

    public void setMem(Double mem) {
        this.mem = mem;
    }

    public Long getUptime() {
        return uptime;
    }

    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }
}
