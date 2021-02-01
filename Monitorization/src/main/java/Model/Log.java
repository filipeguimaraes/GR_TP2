package Model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * Classe que trata da leitura e escrita do ficheiro de log.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Log {
    // Objeto do ficheiro
    private final File file;
    // Stream de escrita do ficheiro
    private DataOutputStream dataOutputStream;

    /**
     * Construtor da class. Recebe o endereço e a porta a monitorizar e cria o ficheiro
     * com o nome log+"::"+endereço+":"+porta+"::".Data e hora de criação"+".txt" na
     * diretoria atual.
     *
     * @param address Endereço que se está a monitorizar
     * @param port Porta que se está a monitorizar
     * @throws IOException Caso não seja possivel criar o ficheiro
     */
    public Log(String address, String port) throws IOException {
        Path currentRelativePath = Paths.get("");
        String relativepath = currentRelativePath.toAbsolutePath().toString();
        String path = relativepath
                + "/log::"
                + address
                + ":"
                + port
                + "::"
                + LocalDateTime.now().format(ISO_LOCAL_DATE_TIME)
                + ".txt";
        this.file = new File(path);
        //noinspection ResultOfMethodCallIgnored
        this.file.createNewFile();

    }

    /**
     * Método que abre o stream de escrita para o ficheiro.
     *
     * @throws FileNotFoundException Caso não consiga abrir o stream de escrita.
     */
    public void open() throws FileNotFoundException {
        dataOutputStream = new DataOutputStream(new FileOutputStream(file, true));
    }

    /**
     * Método que adiciona ao ficheiro a linha pretendida.
     *
     * @param text Linha de texto.
     * @throws IOException Caso não consiga escrever.
     */
    public void append(String text) throws IOException {
        dataOutputStream.write((text + '\n').getBytes(StandardCharsets.UTF_8));
        dataOutputStream.flush();
    }

    /**
     * Método que fecha o stream de escrita para o ficheiro.
     *
     * @throws IOException Caso não consiga fechar a stream de escrita.
     */
    public void close() throws IOException {
        this.dataOutputStream.close();
    }

}
