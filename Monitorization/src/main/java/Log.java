import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/*
 * Class para escrever o log
 * */
public class Log {

    final String path;
    final File file;

    public Log() {
        this.path = "log_" + LocalDateTime.now().format(ISO_LOCAL_DATE_TIME) + ".txt";
        this.file = new File(path);
        try {
            //Criar ficheiro
            //noinspection ResultOfMethodCallIgnored
            this.file.createNewFile();
        } catch (IOException ignored) {
        }
        //obrigar o ficheiro a ser criado
        //noinspection ResultOfMethodCallIgnored
        file.canWrite();
    }


    public void append(String text) throws IOException {
        FileWriter fr = new FileWriter(this.file, true);
        BufferedWriter br = new BufferedWriter(fr);
        br.write(text+'\n');
        br.flush();
        fr.flush();
        br.close();
        fr.close();

        //noinspection ResultOfMethodCallIgnored
        this.file.canWrite();
    }


}
