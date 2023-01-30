package chat;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


import utils.PropertyHandler;

class ChatServer {

    String serverIP;
    int serverPort;
    public static ArrayList<NodeInfo> participants = new ArrayList<>();

    private ServerSocket server = null;

    public ChatServer(String propertiesFile) {

        Properties properties = null;

        // open the server socket
        try {
            server = new ServerSocket(serverPort);
            properties = new PropertyHandler(propertiesFile);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() throws IOException {
        while(true)
        {
            new ChatServerWorker(server.accept()).start();
        }
    }

    public static void main(String[] args) throws IOException {
/*
        // gather variables
        Properties prop = getServerInfo("config/ServerNodeDefaults.properties");
        String serverIP = prop.getProperty("SERVER_IP");
        int serverPort = Integer.parseInt(prop.getProperty("SERVER_PORT"));

        // run the server
        new ChatServer(serverIP, serverPort).runServerLoop();*/
        String propertiesFile = null;

        // read any properties files as arguments
        try {
            propertiesFile = args[0];
        } catch (ArrayIndexOutOfBoundsException ex) {
            propertiesFile = "CentralServer/config/ServerNodeDefaults.properties";
        }

        // start ChatNode
        (new ChatServer(propertiesFile)).run();

    }

}