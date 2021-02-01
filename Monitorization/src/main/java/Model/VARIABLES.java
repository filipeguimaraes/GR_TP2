package Model;

/**
 * Classe que contém variáveis estáticas para simplificar o acesso à MIB.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class VARIABLES {
    /**
     * Uptime do sistema
     */
    static String HRSYSTEMUPTIME = ".1.3.6.1.2.1.25.1.1.0";
    /**
     * Nome dos programas a correr
     */
    static String HRSWRUNNAME = ".1.3.6.1.2.1.25.4.2.1.2";
    /**
     * CPU dos programas a correr
     */
    static String HRSWRUNPERFCPU = ".1.3.6.1.2.1.25.5.1.1.1";
    /**
     * Memória dos programas a correr
     */
    static String HRSWRUNPERFMEM = ".1.3.6.1.2.1.25.5.1.1.2";
    /**
     * Memória RAM total
     */
    static String HRMEMORYSIZE = ".1.3.6.1.2.1.25.2.2.0";
}
