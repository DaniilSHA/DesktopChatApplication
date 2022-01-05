package client;

import network.TCPConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AuthorizationListener implements ActionListener {

    private final ClientWindow clientWindow;
    private final AuthorizationFrame authorizationFrame;
    private final JTextField fieldNickname;
    private final JTextField fieldHost;
    private final JTextField fieldPort;

    private String ipAddr;
    private int port;
    private String nickname;

    public AuthorizationListener(ClientWindow clientWindow, AuthorizationFrame authorizationFrame) {
        this.clientWindow = clientWindow;
        this.authorizationFrame = authorizationFrame;
        this.fieldNickname = authorizationFrame.getFieldNickname();
        this.fieldHost = authorizationFrame.getFieldHost();
        this.fieldPort = authorizationFrame.getFieldPort();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (fieldHost.getText().equals("")) {
            JOptionPane.showMessageDialog(authorizationFrame, "ERROR: empty host field");
            return;
        }
        if (fieldPort.getText().equals("")) {
            JOptionPane.showMessageDialog(authorizationFrame, "ERROR: empty port field");
            return;
        }
        if (fieldNickname.getText().equals("")) {
            JOptionPane.showMessageDialog(authorizationFrame, "ERROR: empty nickname field");
            return;
        }
        try {
            clientWindow.setConnection(new TCPConnection(clientWindow, fieldHost.getText(), Integer.parseInt(fieldPort.getText())));
            JOptionPane.showMessageDialog(authorizationFrame, "Successfully connected");
            clientWindow.setNickname(fieldNickname.getText());
            authorizationFrame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(authorizationFrame, "ERROR: port field is incorrect");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(authorizationFrame, "ERROR: connection failed");
        }
    }
}
