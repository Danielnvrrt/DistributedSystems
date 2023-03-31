import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import transaction.comm.Message;
import transaction.comm.MessageTypes;

package transaction.client;

/**
 *
 * @author Usuario
 */
public class TransactionServerProxy implements MessageTypes {
    
    String host = null;
    int port;
    
    private Socket dbConnection = null;
    private ObjectOutputStream writeToNet = null;
    private ObjectInputStream readFromNet = null;
    private Integer transactionID = 0;
    
    /*
    *Constructor for serve proxy
    *
    */
    TransactionServerProxy(String host, int port){
        // contructs the server using the given information
        this.host = host;
        this.port = port;
    }
    
    //opens a transaction
    public int openTransaction() {
        // this is where we will create the transaction
        // that will start a connection and also record the message
        try {
            dbConnection = new Socket(host, port);
            writeToNet = new PbjectOutputStream(dbConnection.getOutputStream());
            readFromNet = new ObjectInputStream(dbConnection.getInputStream());
        } catch (IOException ex) {
            System.out.println("[TransactionServerProxy.openTransaction] Error occured when opening object streams");
            ex.printStackTrace();
        }
        
        try {
            writeToNet.writeObject(new Message(OPEN_TRANSACTION, null));
            transactionID = (Integer) readFromNet.readObject();
        } catch (IOException | ClassNotFoundException | NullPointerException ex) {
            System.out.println("[TransactionServerProxy.openTransaction] Error when writing/reading messages");
            ex.printStackTrace();
        }
        
        // the transaction will return the transaction id
        return transactionID;
    }
    
    // closes a transaction
    // it will request this transaction to close
    public int closeTransaction() {
        // records the message in the transaction
        int returnStatus = TRANSACTION_COMMITTED;
        
        try {
            writeToNet.writeObject(new Message(CLOSE_TRANSACTION, null));
            returnStatus = (Integer) readFromNet.readObject();
            readFromNet.close();
            writeToNet.close();
            dbConnection.close();
        } catch (IOException | ClassNotFoundException | NullPointerException ex) {
            System.out.println("[TransactionServerProxy.closeTransaction] Error occured");
            ex.printStackTrace();
        }
        
        
        return returnStatus;
        // return that the transaction has been committed
    }
    
    //reads a value from an account
    public int read(int accountNumber) throws TransactionAbortedException {
        // create message variable
        Message message = new Message(READ_REQUEST, accountNumber);
        // read the content
        try {
            writeToNet.writeObject(message);
            message = (Message) readFromNet.readObject();
        } catch (IOException | ClassNotFoundException | NullPointerException ex) {
            System.out.println("[TransactionServerProxy.read] Error occured");
            ex.printStackTrace();
        }
        
        if (message.getType() == READ_REQUEST_RESPONSE) {
            return (Integer) message.getContent();
        }
        else {
            throw new TransactionAbortedException();
        }
        
    }
    
    // writes a value to an account
    public void write(int accountNumber, int amount) throws TransactionAbortedException {
        // create message and content variables
        Object[] content = new Object[]{accountNumber, amount};
        Message message = new Message(WRITE_REQUEST, content);
        // write the content
        
        try {
            writeToNet.writeObject(message);
            message = (Message) readFromNet.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("[TransactionServerProxy.openTransaction] Error occured");
            ex.printStackTrace();
            System.err.print("\n\n");
        }
         
        // throw exception if aborted
        if (message.getType() == TRANSACTION_ABORTED) {
            throw new TransactionAbortedException();
        }
    }
}
