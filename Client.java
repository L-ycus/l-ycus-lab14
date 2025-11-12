import java.io.*;
import java.net.*;

public class Client {
   protected Socket sock;

    public Client(String h, int p) {
        try {
            sock = new Socket(h, p);
        }
        catch(Exception e) {
            sock = null;
            System.out.println(e);
        }
    }

    public Socket getSocket() {
        return sock;
    }

    public void handshake(){

    }

    public void disconnect() {

    }

    public String request(String num) {
        return "";
    }
    
}