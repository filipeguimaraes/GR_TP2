package Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe simplificada da presente no modulo 2
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Tradutor {

    public static Process linhaProcess(String linha) {
        return new Process(getRAM(linha), getCPU(linha));
    }

    private static Double getCPU(String linha) {
        Pattern CPUpattern = Pattern.compile("\"cpu\":[0-9]+\\.[0-9]+");
        Matcher CPUmatcher = CPUpattern.matcher(linha);
        if (CPUmatcher.find()) {
            String CPUaux = CPUmatcher.group();
            Pattern CPU2pattern = Pattern.compile("[^(\"cpu\":)].*");
            Matcher CPU2matcher = CPU2pattern.matcher(CPUaux);
            if (CPU2matcher.find()) {
                return Double.parseDouble(CPU2matcher.group());
            } else return null;
        } else return null;
    }

    private static Double getRAM(String linha) {
        Pattern MEMpattern = Pattern.compile("\"mem\":[0-9]+\\.[0-9]+");
        Matcher MEMmatcher = MEMpattern.matcher(linha);
        if (MEMmatcher.find()) {
            String MEMaux = MEMmatcher.group();
            Pattern MEM2pattern = Pattern.compile("[^(\"mem\":)].*");
            Matcher MEM2matcher = MEM2pattern.matcher(MEMaux);
            if (MEM2matcher.find()) {
                return Double.parseDouble(MEM2matcher.group());
            }else return null;
        }else return null;
    }

}
