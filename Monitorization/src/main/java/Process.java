import org.snmp4j.smi.OID;

/*
* Classe com a estrutura de um processo bem como os métodos a eles aplicados
* */
public class Process {
    private final Integer pid;
    private final String name;
    private float mem;
    private float cpu;
    

    public Process(OID oid_pid, String name) {
        this.pid = oid_pid.last();
        this.name = name;
    }

    public void setMem(Integer mem, Integer totalMemory) {
        float memory = (float) mem;
        float total = (float) totalMemory;
        this.mem = memory/total*100;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    @Override
    public String toString() {
        return "{ \"process\" {" +
                "\"pid\":" + pid +
                ", \"name\":\"" + name + '\"' +
                ", \"mem\":" + mem +
                ", \"cpu\":" + cpu +
                "} }";
    }
}
