package appserver.client.fibonacci;

import appserver.comm.Message;
import appserver.comm.MessageTypes;
import appserver.job.Job;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

import utils.PropertyHandler;

/**
 *
 * @author Usuario
 */
public class FibonacciClient extends Thread implements MessageTypes{
    
    String host = null;
    int port;
    int argument;
    
    Properties properties;
    
    public FibonacciClient(String serverPropertiesFile, int argument) {
        this.argument = argument;
        
        try {
            properties = new PropertyHandler(serverPropertiesFile);
            host = properties.getProperty("HOST");
            System.out.println("[FibonacciClient.FibonacciClient] Host: " + host);
            port = Integer.parseInt(properties.getProperty("PORT"));
            System.out.println("[FibonacciClient.FibonacciClient] Port: " + port);            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    public void run() {
        try {
            // connect to application server
            Socket server = new Socket(host, port);
            
            // hard-coded string of class, aka tool name ... plus one argument
            String classString = "appserver.job.impl.fibonacci.Fibonacci";
            Integer number = argument;
            
            // create job and job request message
            Job job = new Job(classString, number);
            Message message = new Message(JOB_REQUEST, job);
            
            // sending hob out to the application server in a message
            ObjectOutputStream writeToNet = new ObjectOutputStream(server.getOutputStream());
            writeToNet.writeObject(message);
            
            // reading result back in from application server
            // for simplicity, the result is not encapsulated in a message
            ObjectInputStream readFromNet = new ObjectInputStream(server.getInputStream());
            Integer result = (Integer) readFromNet.readObject();
            System.out.println("Fibonacci of " + argument + ": " + result);
        } catch (Exception ex){
            System.err.println("[FibonacciClient.run] Error occurred");
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        for (int i = 46; i > 0; i--){
            (new FibonacciClient("../../config/Server.properties", i)).start();
        }            
    }           
}
