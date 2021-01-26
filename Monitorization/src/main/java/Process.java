import org.snmp4j.smi.OID;

/*
 * Classe com a estrutura de um processo bem como os métodos a eles aplicados
 * */
public class Process {
    private final Integer pid;
    private final String name;
    private float mem;
    private float cpu;
    private float centiceconds;


    public Process(OID oid_pid, String name) {
        this.pid = oid_pid.last();
        this.name = name;
    }

    public void setMem(Integer mem, Integer totalMemory) {
        float memory = (float) mem;
        float total = (float) totalMemory;
        this.mem = memory / total * 100;
    }

    public void setCpu(Process old, int polling) {
        if (old == null) {
            this.cpu = 0;
        } else {
            float oldCentiseconds = old.getCenticeconds();
            float cpu = (this.centiceconds - oldCentiseconds) / polling;
            this.cpu = cpu;
        }

    }

    public float getCenticeconds() {
        return centiceconds;
    }

    public void setCenticeconds(float centiceconds) {
        this.centiceconds = centiceconds;
    }

    public Integer getPid() {
        return pid;
    }

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
