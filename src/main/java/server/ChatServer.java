package server;

import network.TCPConnection;
import network.TCPConnectionListener;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChatServer implements TCPConnectionListener {
    public static void main(String[] args) {
        new ChatServer();
    }

    private final LinkedHashMap<TCPConnection, String> connections = new LinkedHashMap<>();
    private Logger logger;

    private ChatServer() {
        settingLogger();
        logger.info("Server running...");
        System.out.println("Server running...");

        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    logger.error("TCPConnection exception" + e);
                    System.out.println("TCPConnection exception");
                }
            }
        } catch (IOException e) {
            logger.fatal("ServerSocketException" + e);
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
            logger.info("Client [" + value.substring(20) + "] connected: {" + tcpConnection + "}");
        } else {
            sendToAllConnections(value);
            logger.info("Client [" + connections.get(tcpConnection) + "] msg: " + value);
        }
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        logger.info("Client [" + connections.get(tcpConnection) + "] disconnected: {" + tcpConnection + "}");
        connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: " + tcpConnection);
        sendValidConnectionsInfo();
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        logger.error("TCPConnection exception" + e);
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

    private void settingLogger() {
        try {
            Path logDir = Paths.get("serverLogs");
            if (!Files.exists(logDir)) {
                Files.createDirectory(logDir);
            }
            Path logSettings = Paths.get(logDir.toString() + File.separator + "log4j.properties");
            if (!Files.exists(logSettings)) {
                Files.createFile(logSettings);

                Path logInfoSaving = Paths.get(logDir.toAbsolutePath().toString() + File.separator + "log4j-INFO.logs");
                Files.createFile(logInfoSaving);
                PrintWriter writer = new PrintWriter(new FileWriter(logSettings.toFile()));
                writer.println("log4j.rootLogger=INFO, file-INFO, file-FATAL, file-ERROR");
                writer.println("log4j.appender.file-INFO=org.apache.log4j.RollingFileAppender");
                writer.println("log4j.appender.file-INFO.File=" + logInfoSaving.toString().replaceAll("\\\\", "\\\\\\\\"));
                writer.println("log4j.appender.file-INFO.threshold=INFO");
                writer.println("log4j.appender.file-INFO.MaxFileSize=10KB");
                writer.println("log4j.appender.file-INFO.MaxBackupIndex=4");
                writer.println("log4j.appender.file-INFO.layout=org.apache.log4j.PatternLayout");
                writer.println("log4j.appender.file-INFO.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
                writer.flush();

                Path logFatalSaving = Paths.get(logDir.toAbsolutePath().toString() + File.separator + "log4j-FATAL.logs");
                Files.createFile(logFatalSaving);
                writer.println("log4j.appender.file-FATAL=org.apache.log4j.RollingFileAppender");
                writer.println("log4j.appender.file-FATAL.File=" + logFatalSaving.toString().replaceAll("\\\\", "\\\\\\\\"));
                writer.println("log4j.appender.file-FATAL.threshold=FATAL");
                writer.println("log4j.appender.file-FATAL.MaxFileSize=10KB");
                writer.println("log4j.appender.file-FATAL.MaxBackupIndex=4");
                writer.println("log4j.appender.file-FATAL.layout=org.apache.log4j.PatternLayout");
                writer.println("log4j.appender.file-FATAL.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
                writer.flush();

                Path logErrorSaving = Paths.get(logDir.toAbsolutePath().toString() + File.separator + "log4j-ERROR.logs");
                Files.createFile(logErrorSaving);
                writer.println("log4j.appender.file-ERROR=org.apache.log4j.RollingFileAppender");
                writer.println("log4j.appender.file-ERROR.File=" + logErrorSaving.toString().replaceAll("\\\\", "\\\\\\\\"));
                writer.println("log4j.appender.file-ERROR.threshold=ERROR");
                writer.println("log4j.appender.file-ERROR.MaxFileSize=10KB");
                writer.println("log4j.appender.file-ERROR.MaxBackupIndex=4");
                writer.println("log4j.appender.file-ERROR.layout=org.apache.log4j.PatternLayout");
                writer.println("log4j.appender.file-ERROR.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
                writer.flush();
            }
            PropertyConfigurator.configure(logSettings.toAbsolutePath().toString());
            logger = LogManager.getLogger(ChatServer.class);
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

}
