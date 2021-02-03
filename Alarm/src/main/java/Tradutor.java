import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Tradutor {

    public static Process linhaProcess(String linha) {
        return new Process(getPID(linha), getName(linha), getRAM(linha), getCPU(linha));
    }

    private static Integer getPID(String linha) {
        Pattern PIDpattern = Pattern.compile("\"pid\":[0-9]+", Pattern.MULTILINE);
        Matcher PIDmatcher = PIDpattern.matcher(linha);
        if (PIDmatcher.find()) {
            String PIDaux = PIDmatcher.group();
            Pattern PID2pattern = Pattern.compile("[0-9]+");
            Matcher PID2matcher = PID2pattern.matcher(PIDaux);
            if (PID2matcher.find()) {
                return Integer.parseInt(PID2matcher.group());
            } else return null;
        } else return null;
    }

    private static String getName(String linha) {
        Pattern NAMEpattern = Pattern.compile("\"name\":\"[^\"]*\"");
        Matcher NAMEmatcher = NAMEpattern.matcher(linha);
        if (NAMEmatcher.find()) {
            String NAMEaux = NAMEmatcher.group();
            Pattern NAME2pattern = Pattern.compile("[^(\"name\":)].*[^\"]");
            Matcher NAME2matcher = NAME2pattern.matcher(NAMEaux);
            if (NAME2matcher.find()) {
                return NAME2matcher.group();
            } else return null;
        } else return null;
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
