package transaction.server;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import transaction.server.transaction.TransactionManager;
/**
 *
 * @author Daniel
 */
public class TransactionServer {

    String serverIP;
    int serverPort;

    private ServerSocket server = null;

    
    // Transaction Server constructor, initialize the server socket
    public TransactionServer(String propertiesFile) {
        
        Properties p = new Properties();
        try {
            p.load(new FileReader(propertiesFile));
        }
        catch (IOException ex){
            System.exit(1);
        }
        
        System.out.println(p.getProperty("N_ACCOUNTS"));
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        try {
            this.serverPort = Integer(p.getProperty("PORT"));
        }
        catch (IOException ex) {
            System.exit(1);
        }
        
    }

    // Create the new TransactionManager
    public void run() throws IOException {
        while(true)
        {
            // Do we create a new transactionManager every time?
            
            new TransactionManager().runTransaction(server.accept());
        }
    }
    
    // Main method that creates a new instance of the Transaction Server
    // with the values taken from the configuration file 
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            //new TransactionServer("10.21.41.190", 801).run();
            TransactionServer x = new TransactionServer("config/serverPropertiesFile.properties");
        }
        catch (RuntimeException ex) {
            System.exit(1);
        }
        
    }
    
}
