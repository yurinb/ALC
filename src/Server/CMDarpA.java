package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Yuri
 * 
 * 
 * +++++++++++++++++++++++++++++++++++ NOT DONNE YET ++++++++++++++++++++++++++++++++++++++++
 * 
 * 
 */
public class CMDarpA {

    static String localIP;
    static ArrayList<String> ip;

    public static void main(String[] args) {
        new CMDarpA();
    }

    public CMDarpA() {
        Process cmd;
        String retorno = null;
        Scanner sc = null;
        ip = new ArrayList<>();
        try {
            cmd = Runtime.getRuntime().exec("arp -a");
            BufferedReader br = new BufferedReader(new InputStreamReader(cmd.getInputStream()));
            retorno = br.readLine();
            boolean firstTimeLoop = true;
            while ((retorno = br.readLine()) != null) {
                System.out.println(retorno);
                sc = new Scanner(retorno);
                ip.add(sc.next());
                if (firstTimeLoop) {
                    ip.set(0, sc.next());
                    firstTimeLoop = false;
                }
            }
        } catch (Exception e) {

        }

        this.localIP = ip.get(0);
        System.out.println();
        System.out.println("IP LOCAL: "+localIP);
        for (int i = 2; i < ip.size(); i++) {
            System.out.print(ip.get(i) + "\n");
        }
        ip.remove(0);
        ip.remove(0);
    }

}
