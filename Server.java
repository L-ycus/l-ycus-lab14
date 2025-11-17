import java.util.*;
import java.time.*;
import java.net.*;
import java.io.*;

public class Server extends Thread{
    protected int numClients;
    protected ServerSocket serverSock;
    private ArrayList<LocalDateTime> connectedTimes = new ArrayList<>();
    
    public Server(int n) throws IOException {
        numClients = n;
    }

    private class serverClients extends Thread {
        private Socket client;

        public serverClients(Socket s) {
            client = s;
        }

        public void run() {
            try(
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            ) {
                String tempPass = in.readLine();
                if( !"12345".equals(tempPass)) {
                    out.println("INVALID PASSWORD!");
                    client.close();
                    return;
                }

                out.println("PASSWORD ACCEPTED!");
                String message = in.readLine();
                if(message == null) {
                    client.close();
                    return;
                }

                String response = request(message);
                out.println(response);
            } catch (IOException e) {
                System.err.println("RUN SERVER NOT WORKING!!!");
            } finally {
                try {
                    client.close();
                } catch (Exception e){
                    System.err.println(e.getMessage());
                }

            }
        }
    }

    public void serve(int n) {
        try {
            serverSock = new ServerSocket(n);
            int count = 0;

            while(count < numClients) {
                Socket client = serverSock.accept();

                synchronized(connectedTimes) {
                    connectedTimes.add(LocalDateTime.now());
                }

                serverClients clientThread = new serverClients(client);
                clientThread.start();
                count++;
            }
        } catch (IOException e) {
            System.err.println("SERVE NOT WORKING: " + e.getMessage());
        }
    }

    public String request(String str) {
        StringBuilder ret = new StringBuilder();
        int num = Integer.parseInt(str);

        for(int i = 2; i * i <= num; i++) {
            while(num %i == 0) {
                ret.append(i).append(" ");
                num /= i;
            }
        }

        if(num > 1) {
            ret.append(num);
        }

        return ret.toString();
    }

    public void disconnect() {
        try {
            if(serverSock != null) {
                serverSock.close();
            }
        } catch (IOException e) {
            System.err.println("DISCONNECT ERROR" + e.getMessage());
        }
    }

    public ArrayList<LocalDateTime> getConnectedTimes() {
        ArrayList<LocalDateTime> temp;

        synchronized(connectedTimes) {
            temp = new ArrayList<>(connectedTimes);
        }
        Collections.sort(temp);
        return temp;
    }
}
