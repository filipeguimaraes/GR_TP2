package Model;

public class ProcessGroup {
    /**
     *
     */
    private Integer pid;
    /**
     *
     */
    private String name;
    /**
     *
     */
    private Double cpu;
    /**
     *
     */
    private Double mem;
    /**
     *
     */
    private Long uptime;

    /**
     * @param pid
     * @param name
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
