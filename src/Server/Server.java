package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    static String serverVersion = "1.3";
    public static ArrayList<Client> clientAtServer = new ArrayList<>();

    public Server() throws IOException {
        ServerSocket server = new ServerSocket(5000);
        System.out.println("server rodando...");

        Thread waitingClient = new Thread() {
            @Override
            public void run() {
                while (true) {
                    Socket socket;
                    try {
                        System.out.println("Waiting client...");
                        socket = server.accept();
                        System.out.println("*Client connected*");
                        new Thread(new ServerClientListener(socket)).start();
                        PrintWriter p = new PrintWriter(socket.getOutputStream());
                        Client c = new Client(socket, p);
                        clientAtServer.add(c);

                    } catch (IOException ex) {
                        System.out.println("Failure on inicialize server.");
                    }
                }
            }
        };
        waitingClient.start();
    }

    // PING FOR CONNECTION MONITORING
    

    static void encaminharParaTodos(String txt) {
        for (int i = 0; i < clientAtServer.size(); i++) {
            try {
                clientAtServer.get(i).writer.println(txt);
                clientAtServer.get(i).writer.flush();
            } catch (Exception e) {
            }
        }
    }
//                 NOT DONE YET

    private void encaminharParaAlguem(String txt, PrintWriter client) {
        try {
            //clientsPW.get().println(txt);
            //   clientsPW.get(i).flush();
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) throws IOException {

        new Server();

    }

}
