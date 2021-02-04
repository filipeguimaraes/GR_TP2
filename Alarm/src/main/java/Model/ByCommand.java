package Model;

import java.io.IOException;

public class ByCommand {
    public void send(String command,String subject, String msg) throws IOException {
        Runtime.getRuntime().exec(command+" \""+subject+"\" \""+msg+"\"");
    }


}
