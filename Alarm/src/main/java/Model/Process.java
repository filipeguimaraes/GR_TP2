package Model;

/**
 * Classe com a estrutura simplificada de um processo bem como os métodos a eles aplicados
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Process {
    private final Double mem;
    private final Double cpu;


    public Process(Double mem, Double cpu) {
        this.mem = mem;
        this.cpu = cpu;
    }

    public Double getMem() {
        return mem;
    }

    public Double getCpu() {
        return cpu;
    }
}
