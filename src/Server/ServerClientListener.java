package Server;

import static Server.Server.clientAtServer;
import static Server.Server.encaminharParaTodos;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yuri
 */
public class ServerClientListener extends TimerTask {

    private Socket clientSocket;

    private int ClientNumberInList;

    private Scanner scanner;

    public ServerClientListener(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.ClientNumberInList += 1;
        scanner = new Scanner(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            String txt;
            while ((txt = scanner.nextLine()) != null) {
                System.out.println("scaneou " + txt);
                if (txt.contains("@ping@")) { //manter conexao ativa
                    System.out.println("@ping@");
                    //Server.encaminharParaTodos("@ping@");
                    for (int i = 0; i < Server.clientAtServer.size(); i++) {
                            System.out.println("client num " + i);
                            System.out.println(txt.substring(6) + " == " + (Server.clientAtServer.get(i).nickname));
                            if (Server.clientAtServer.get(i).nickname.equals(txt.substring(6)) && Server.clientAtServer.get(i).nickname != null) {
                                Server.clientAtServer.get(i).pingRespost += 1;
                            }
                    }
                } else if (txt.contains("@setClientName@")) {

                    for (int i = 0; i < Server.clientAtServer.size(); i++) {

                        int ipLength = Integer.parseInt(txt.substring(15, 17));
                        System.out.println("ip length: " + txt.substring(15, 17));

                        if (Server.clientAtServer.get(i).nickname == null && Server.clientAtServer.get(i).ip.equals(txt.substring(17, (17 + ipLength)))) {

                            Server.clientAtServer.get(i).nickname = txt.substring(30);
                            System.out.println("nick name for server: " + Server.clientAtServer.get(i).nickname);
                            System.out.println("client number > " + i);
                            Timer pingTimer = new Timer();
                            pingTimer.scheduleAtFixedRate(new PingsCount(clientAtServer.get(i)), 6000, 6000);
                        }
                    }
                } else if (txt.contains("@list@")) {
                    Server.encaminharParaTodos("\n---------Clients Online---------");
                    for (int i = 0; i < Server.clientAtServer.size(); i++) {
                        Server.encaminharParaTodos(Server.clientAtServer.get(i).nickname + " - " + Server.clientAtServer.get(i).ip);
                    }
                    Server.encaminharParaTodos("--------------------------------\n");
                } else if (txt.contains("@version@")) {
                    System.out.println("VERSION > " + txt.substring(9, 12));
                    if (!(txt.substring(9, 12).equals(Server.serverVersion))) {
                        System.out.println("CLIENTE DESATUALIZADO");
                        for (int i = 0; i < Server.clientAtServer.size(); i++) {
                            if (Server.clientAtServer.get(i).nickname.equals(txt.substring(12, (txt.length() - 2)))) {
                                Server.encaminharParaTodos(txt.substring(12) + ", atualize seu cliente.");
                                Server.clientAtServer.get(i).socket.close();
                            }
                        }
                    }
                } else {
                    Server.encaminharParaTodos("");
                    Server.encaminharParaTodos(txt);
                }
            }
        } catch (Exception e) {
        }
    }

    class PingsCount extends TimerTask {

        private Client client;

        public PingsCount(Client client) {
            this.client = client;
        }

        @Override
        public void run() {
            if (client.nickname != null) {
                client.pingRespost -= 1;
                System.out.println(client.nickname + "pingRespost = " + client.pingRespost);
                if (client.pingRespost < 0) {
                    try {
                        client.socket.close();
                        encaminharParaTodos(client.nickname + " has disconected from the server.");
                        System.out.println("Client disconnected - " + client.nickname);
                        clientAtServer.remove(client);
                        this.cancel();
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }

    }

}
