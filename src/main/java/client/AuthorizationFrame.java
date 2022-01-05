package client;

import javax.swing.*;
import java.awt.*;

public class AuthorizationFrame extends JFrame {

    private final JButton buttonOk = new JButton("OK");
    private final JTextField fieldNickname = new JTextField();
    private final JTextField fieldHost = new JTextField();
    private final JTextField fieldPort = new JTextField();
    private final JLabel labelNickname = new JLabel("enter your nickname: ");
    private final JLabel labelHost = new JLabel("enter server HOST: ");
    private final JLabel labelPort = new JLabel("enter server PORT: ");

    public AuthorizationFrame (){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(300, 600);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        add(buttonOk, BorderLayout.NORTH);

        JPanel generalInfoPanel = new JPanel();
        generalInfoPanel.setLayout(new BorderLayout());

        JPanel nicknamePanel = new JPanel();
        nicknamePanel.setLayout(new FlowLayout());
        nicknamePanel.add(labelNickname, fieldNickname);

        JPanel hostPanel = new JPanel();
        hostPanel.setLayout(new FlowLayout());
        hostPanel.add(labelHost, fieldHost);

        JPanel portPanel = new JPanel();
        portPanel.setLayout(new FlowLayout());
        portPanel.add(labelPort, fieldPort);

        generalInfoPanel.add(hostPanel, BorderLayout.NORTH);
        generalInfoPanel.add(portPanel, BorderLayout.CENTER);
        generalInfoPanel.add(nicknamePanel, BorderLayout.SOUTH);

        add(generalInfoPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
