package Server;

import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    String nickname = null;
    
    private static String id = null;
    
    Socket socket;
    
    String ip;
    
    int pingRespost;
    
    PrintWriter writer;
    
    public Client(Socket socket, PrintWriter pwriter){
        this.socket = socket;
        this.writer = pwriter;
        this.ip = socket.getInetAddress().getHostAddress();
        this.id += 1;
    }
    
    
}
