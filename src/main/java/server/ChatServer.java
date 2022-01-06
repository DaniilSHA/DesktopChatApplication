package server;

import network.TCPConnection;
import network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer implements TCPConnectionListener {
    public static void main(String[] args) {
        new ChatServer();
    }

    private final LinkedHashMap<TCPConnection, String> connections = new LinkedHashMap<>();

    private ChatServer() {
        System.out.println("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.put(tcpConnection, "");
        sendToAllConnections("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        if (value.startsWith("[connectionsListMSG]")) {
            connections.replace(tcpConnection, value.substring(20));
            sendValidConnectionsInfo();
        } else sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: " + tcpConnection);
        sendValidConnectionsInfo();
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception" + e);
    }

    private void sendToAllConnections(String value) {
        System.out.println(value);
        for (Map.Entry<TCPConnection, String> pair : connections.entrySet()) {
            pair.getKey().sendString(value);
        }
    }

    private void sendValidConnectionsInfo() {
        StringBuilder msg = new StringBuilder();
        msg.append("[connectionsListMSG]");
        for (Map.Entry<TCPConnection, String> pair : connections.entrySet()) {
            msg.append(pair.getValue()).append("-");
        }
        sendToAllConnections(msg.toString());
    }

}
