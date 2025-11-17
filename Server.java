import java.util.*;
import java.time.*;
import java.net.*;
import java.io.*;

public class Server extends Thread{
    protected int numClients;
    protected ServerSocket serverSock;
    private ArrayList<LocalDateTime> connectedTimes = new ArrayList<>();
    
    public Server(int n){
        numClients = n;

        try{
            serverSock = new ServerSocket(n);
        } catch(IOException e) {
            System.err.println("SERVER CONSTRUCT ERROR: " + e.getMessage());
        }
    }

    private class serverClients extends Thread {
        private Socket client;

        public serverClients(Socket s) {
            client = s;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            
                String tempPass = in.readLine();
                if( !"12345".equals(tempPass)) {
                    out.println("couldn't handshake");
                    client.close();
                    return;
                }

                synchronized(connectedTimes) {
                    connectedTimes.add(LocalDateTime.now());
                }

                //out.println("PASSWORD ACCEPTED!");
                String message = in.readLine();
                if(message == null) {
                    client.close();
                    return;
                }

                String response;
                try {
                    response = request(message);
                } catch (Exception e) {
                    response = "AHHHH exception in run server";
                }

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
        int count = 0;

            while(count < n) {
                try {
                    Socket client = serverSock.accept();

                //serverClients clientThread = new serverClients(client);
                //clientThread.start();
                    new serverClients(client).start();
                    count++;
                } catch (IOException e) {
                    System.err.println("SERVE NOT WORKING :( " + e.getMessage());
                }
            }
    }

    public String request(String str) {
        try {
            int num = Integer.parseInt(str);
            int count = 0;

            for(long i = 1; i * i <= num; i++) {
                if(num %i == 0) {
                    count++;
                    if(i != num / i) {
                        count++;
                    }
                }
            }

            return "The number " + num + " has " + count + " factors";
        } catch (Exception e) {
            return "There was an exception on the server";
        }
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
