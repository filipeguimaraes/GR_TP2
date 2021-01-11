import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/*
 * Class para escrever o log
 * */
public class Log {

    final String path;
    final File file;
    private DataOutputStream dataOutputStream;

    public Log(String address,String port) throws IOException {
        Path currentRelativePath = Paths.get("");
        String relativepath = currentRelativePath.toAbsolutePath().toString();
        this.path = relativepath+"/log::"+address+":"+port+"::"+LocalDateTime.now().format(ISO_LOCAL_DATE_TIME)+".txt";
        this.file = new File(path);

        //Criar ficheiro
        //noinspection ResultOfMethodCallIgnored
        this.file.createNewFile();

    }

    public void open() throws FileNotFoundException {
        dataOutputStream = new DataOutputStream(new FileOutputStream(file,true));
    }


    public void append(String text) throws IOException {
        dataOutputStream.write((text+'\n').getBytes(StandardCharsets.UTF_8));
        dataOutputStream.flush();
    }

    public void close() throws IOException {
        this.dataOutputStream.close();
    }

}
