

/*
 * Classe com a estrutura de um processo bem como os métodos a eles aplicados
 * */
public class Process {
    private final Integer pid;
    private final String name;
    private final Integer mem;
    private final Integer cpu;


    public Process(Integer pid, String name, Integer mem, Integer cpu) {
        this.pid = pid;
        this.name = name;
        this.mem = mem;
        this.cpu = cpu;
    }

}
