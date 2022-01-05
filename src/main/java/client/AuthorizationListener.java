package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthorizationListener implements ActionListener {

    private final ClientWindow clientWindow;
    private String ipAddr;
    private int port;
    private String nickname;

    public AuthorizationListener(ClientWindow clientWindow) {
        this.clientWindow = clientWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
