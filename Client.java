import java.io.*;
import java.net.*;
import java.net.*;

public class Client {
   protected String host;
   protected int port;
   protected Socket sock;
   protected BufferedReader in;
   protected PrintWriter out;
   
    public Client(String h, int p) {
        host = h;
        port = p;

        try {
            sock = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
        } catch (IOException e) {
           System.err.println("CONSTRUCTOR FAIL! " + e.getMessage());
        }
    }

    public Socket getSocket() {
        return sock;
    }

    public void handshake(){
       try {
            out.println("12345");

            out.flush();
       } catch (Exception e) {
             System.err.println("HANDSHAKE EXCEPT: " + e.getMessage());
       }

    }

    public void disconnect() {
        try {
            if(sock != null) {
                sock.close();
            }
        } catch(IOException e) {
            System.err.println("DISCONNECT FAIL!!");
        }
    }

    public String request(String num) {
        try {
            out.println(num);
            out.flush();
            return in.readLine();
        } catch (IOException e) {
            System.err.println("ERROR REQUEST! " + e.getMessage());
            return null;
        }
    }
    
}