import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Class para escrever o log
 * */
public class Log {

    private final String path;
    private File file = null;

    public Log(String path) throws IOException {
        this.path = path;
        this.file = new File(path);
        if (!this.file.createNewFile()) {
            throw new IOException("Ficheiro já existe!");
        }
    }



    public void append(String text) throws IOException {
        FileWriter fr = new FileWriter(this.file, true);
        BufferedWriter br = new BufferedWriter(fr);
        br.write(text);
        br.close();
        fr.close();
    }


}
