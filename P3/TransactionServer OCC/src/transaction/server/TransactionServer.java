package transaction.server;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Daniel
 */
public class TransactionServer {

    String serverIP;
    int serverPort;

    private ServerSocket server = null;

    
    // Transaction Server constructor, initialize the server socket
    public TransactionServer(String serverIP, int serverPort) {
        
    }

    // Create the new TransactionManager
    public void run() throws IOException {
        while(true)
        {
            new TransactionManager(server.accept()).start();
        }
    }
    
    // Main method that creates a new instance of the Transaction Server
    // with the values taken from the configuration file 
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
