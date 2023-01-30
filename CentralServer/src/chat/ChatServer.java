package chat;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


import utils.PropertyHandler;

class ChatServer{

    String serverIP;
    int serverPort;
    public static ArrayList<NodeInfo> participants = new ArrayList<>();

    private ServerSocket server = null;

    /*public ChatServer(String propertiesFile) throws IOException {

        Properties properties = null;

        // open the server socket
        try {
            properties = new PropertyHandler(propertiesFile);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Could not open the properties file", ex);
            System.exit(1);
        }
        serverPort = 0;
        try {
            serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        }
        // if port number was not able to be read then close the program
        catch (NumberFormatException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Could not read server port", ex);
            System.exit(1);
        }
        try {
            server = new ServerSocket(serverPort);
            System.out.println("ServerSocket created at " + serverPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
    public ChatServer(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;

        // open the server socket
        try {
            server = new ServerSocket(serverPort);
            System.out.println("Chat server socket open!");
            System.out.println(serverPort);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Server socket failed to open.", ex);
        }
    }


    public void run() throws IOException{
        while(true) {
            System.out.println("hello");
            new ChatServerWorker(server.accept()).start();
            System.out.println("bye");

        }
    }

    public static void main(String[] args) throws IOException {
/*

        String propertiesFile = null;

        // read any properties files as arguments
        try {
            propertiesFile = args[0];
        } catch (ArrayIndexOutOfBoundsException ex) {
            propertiesFile = "CentralServer/config/ServerNodeDefaults.properties";
        }

        // start ChatNode
        (new ChatServer(propertiesFile)).run();*/
        String propertiesFile = "CentralServer/config/ServerNodeDefaults.properties";
        Properties prop = new PropertyHandler(propertiesFile);
        String serverIP = prop.getProperty("SERVER_IP");
        int serverPort = Integer.parseInt(prop.getProperty("SERVER_PORT"));

        // run the server
        new ChatServer(serverIP, serverPort).run();

    }

}