import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * Class para ler do log
 * */
public class Log {

    final String path;
    private BufferedReader br;

    public Log(String path) {
        this.path = path;
    }

    public void open() throws FileNotFoundException {
        br = new BufferedReader(new FileReader(path));
    }

    public void close() throws IOException {
        br.close();
    }


    public void read() throws IOException, InterruptedException {
        String line = null;
        while (true) {
            line = br.readLine();
            if (line == null) {
                //wait until there is more of the file for us to read
                Thread.sleep(1000);
            } else {
                System.out.println(line);
            }
        }
    }


}

