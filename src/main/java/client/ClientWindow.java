package client;

import network.TCPConnection;
import network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.*;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {

    private static final int CLIENTS = 3;

    public static void main(String[] args) {
        //start 3 examples of clients
        ExecutorService fixedPoolOfClients = Executors.newFixedThreadPool(CLIENTS);
        for (int i = 0; i < CLIENTS; i++) {
            fixedPoolOfClients.submit(new Runnable() {
                @Override
                public void run() {
                    new ClientWindow();
                }
            });
        }
    }

    private static final String IP_ADDR = "localhost";
    private static final int PORT = 8189;
    private TCPConnection connection;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private final JTextArea log = new JTextArea();
    private final JTextField filedNickname = new JTextField("Daniil");
    private final JTextField filedInput = new JTextField();

    private ClientWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        //settings log
        log.setEditable(false);
        log.setLineWrap(true);

        filedInput.addActionListener(this);

        add(log, BorderLayout.CENTER);
        add(filedInput, BorderLayout.SOUTH);
        add(filedNickname, BorderLayout.NORTH);

        setVisible(true);
        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            printMsg("Connection exception: " + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = filedInput.getText();
        if (msg.equals("")) return;
        filedInput.setText(null);
        connection.sendString(filedNickname.getText() + ": " + msg);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsg("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection exception: " + e);
    }

    private synchronized void printMsg(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

}