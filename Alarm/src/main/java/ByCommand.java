import java.io.IOException;

public class ByCommand {
    public void send(String command,String subject, String msg) throws IOException {
        Runtime.getRuntime().exec(command+" \""+subject+"\" \""+msg+"\"");
    }

    public static void main(String[] args) throws IOException {
        ByCommand bc = new ByCommand();
        bc.send("notify-send","hello","Hello world!");
    }

}
