package transaction.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import utils.PropertyHandler;
import utils.NetworkUtilities;
import java.util.Properties;



/**
 *
 * @author Brian
 */
public class TransactionClient extends Thread {
    String host = null;
    int port;
    // create the sender and receiver variables
    private ObjectOutputStream writeToNet = null;
    private ObjectInputStream readFromNet = null;
    private TransactionServerProxy proxy = null;
    // client constructor
    public TransactionClient(String propertiesFile) {
        // get the properties file
        
        Properties properties = null;
	try {
            properties = new PropertyHandler(propertiesFile);
	}
	// if properties were not found then close the program
	catch (IOException ex) {
            //Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Could not open the properties file", ex);
            System.exit(1);
        }
        
        // get the port number for the Receiver
        this.port = 0;
        
	try {
            //this.port = Integer.parseInt(properties.getProperty("PORT"));
            this.port = 801;
	}
        // if port number was not able to be read then close the program
	catch (NumberFormatException ex) {
            //Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Could not read receiver port", ex);
            
            System.exit(1);
	}
	// create the node info for the Client
        this.host = NetworkUtilities.getMyIP();
        this.proxy = new TransactionServerProxy(this.host, this.port);
        
    }
    
    public void run() {
        // start receiver and sender
        
        // create the proxy server
        this.proxy.openTransaction();
        //this.proxy.read(port);
        //this.proxy.read(port);
        //this.proxy.closeTransaction();
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic 
        
        String propertiesFile = "config/propertiesFile.properties";
        System.out.println(System.getProperty("user.dir"));
        new TransactionClient(propertiesFile).run();
    }
    
}
