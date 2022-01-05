package client;

import javax.swing.*;
import java.awt.*;

public class AuthorizationFrame extends JFrame {

    private final JButton buttonOk = new JButton("OK");
    private final JTextField fieldNickname = new JTextField(10);
    private final JTextField fieldHost = new JTextField(10);
    private final JTextField fieldPort = new JTextField(10);
    private final JLabel labelNickname = new JLabel("enter your nickname: ");
    private final JLabel labelHost = new JLabel("enter server HOST: ");
    private final JLabel labelPort = new JLabel("enter server PORT: ");

    public AuthorizationFrame (ClientWindow clientWindow){

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(250, 250);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setTitle("Authorization");
        setResizable(false);

        JPanel generalInfoPanel = new JPanel();
        generalInfoPanel.setLayout(new BorderLayout());

        JPanel nicknamePanel = new JPanel();
        nicknamePanel.setLayout(new FlowLayout());
        nicknamePanel.setPreferredSize(new Dimension(200,50));
        nicknamePanel.add(labelNickname);
        nicknamePanel.add(fieldNickname);

        JPanel hostPanel = new JPanel();
        hostPanel.setLayout(new FlowLayout());
        hostPanel.setPreferredSize(new Dimension(200,50));
        hostPanel.add(labelHost);
        hostPanel.add(fieldHost);

        JPanel portPanel = new JPanel();
        portPanel.setLayout(new FlowLayout());
        portPanel.setPreferredSize(new Dimension(200,50));
        portPanel.add(labelPort);
        portPanel.add(fieldPort);

        generalInfoPanel.add(hostPanel, BorderLayout.NORTH);
        generalInfoPanel.add(portPanel, BorderLayout.CENTER);
        generalInfoPanel.add(nicknamePanel, BorderLayout.SOUTH);

        buttonOk.addActionListener(new AuthorizationListener(clientWindow,this));

        add(generalInfoPanel, BorderLayout.NORTH);
        add(buttonOk, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JTextField getFieldNickname() {
        return fieldNickname;
    }

    public JTextField getFieldHost() {
        return fieldHost;
    }

    public JTextField getFieldPort() {
        return fieldPort;
    }
}
