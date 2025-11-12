import java.util.*;
import java.time.*;

public class Server {
    protected int numClients;

    public Server(int n) {
        numClients = n;
        }

    public void serve(int n) {
        //loop for numClients
        //accept a client via handshake, process client request in a separate thread
    
    }

    public String request(String str) {
        return "";
    }

    public void disconnect() {

    }

    public ArrayList<LocalDateTime> getConnectedTimes() {
        
        return null;
    }
}
