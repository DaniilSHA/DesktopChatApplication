package client;

import network.TCPConnection;
import network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {

    private static final int CLIENTS = 1;

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

    private JLabel nickname;
    private TCPConnection connection;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private final JTextArea log = new JTextArea();
    private final JTextArea connectionsInfo = new JTextArea();
    private final JTextField filedInput = new JTextField();

    private ClientWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setResizable(false);

        //settings log & connectionsInfo
        log.setEditable(false);
        log.setLineWrap(true);
        log.setPreferredSize(new Dimension(400, 350));
        connectionsInfo.setEditable(false);
        connectionsInfo.setLineWrap(true);
        connectionsInfo.setPreferredSize(new Dimension(200, 350));
        connectionsInfo.setBackground(Color.CYAN);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new FlowLayout());
        textPanel.add(log);
        textPanel.add(connectionsInfo);

        filedInput.addActionListener(this);

        add(textPanel, BorderLayout.WEST);
        add(filedInput, BorderLayout.SOUTH);

        //authorization
        new AuthorizationFrame(this);
    }

    public synchronized void setNickname(String nickname) {
        this.nickname = new JLabel("your nickname: " + nickname);
        this.nickname.setHorizontalAlignment(SwingConstants.CENTER);
        add(this.nickname, BorderLayout.NORTH);
        setVisible(true);
        connection.sendString("[connectionsListMSG]" + this.nickname.getText().substring(15));
    }

    public synchronized void setConnection(TCPConnection connection) {
        this.connection = connection;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = filedInput.getText();
        if (msg.equals("")) return;
        if (msg.contains("[connectionsListMSG]")) {
            JOptionPane.showMessageDialog(this, "ERROR: using prohibited combination: [connectionsListMSG]");
            return;
        }
        filedInput.setText(null);
        connection.sendString(nickname.getText().substring(15) + ": " + msg);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsgToLog("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        if (value.startsWith("[connectionsListMSG]")) {
            StringBuilder validConnInfo = new StringBuilder();
            String[] nicknames = value.substring(20).split("-");
            for (int i = nicknames.length - 1; i >= 0; i--) {
                validConnInfo.append("    \u00B7  " + nicknames[i] + "\n");
            }
            printMsgToConnInfo(validConnInfo.toString());
        } else printMsgToLog(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsgToLog("Connection close");
        JOptionPane.showMessageDialog(this, "ERROR: connection failed");
        new AuthorizationFrame(this);
        setVisible(false);
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMsgToLog("Connection exception: " + e);
    }

    private synchronized void sendNickname() {
        connection.sendString("[connectionListMSG]" + nickname.getText().substring(15));
    }

    private synchronized void printMsgToLog(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    private synchronized void printMsgToConnInfo(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                connectionsInfo.setText(null);
                connectionsInfo.append("               Users connected:" + "\n");
                connectionsInfo.append(msg);
            }
        });
    }
}
