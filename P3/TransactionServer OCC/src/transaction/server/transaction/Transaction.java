package transaction.server.transaction;

import transaction.server.TransactionServer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Daniel
 */
public class Transaction {
    
    // transaction ID and OCC specific transaction numbers
    int transactionID;
    int transactionNumber;
    int lastCommittedTransactionNumber;
    
    // the sets of tentative data
    ArrayList<Integer> readSet = new ArrayList<>();
    HashMap<Integer, Integer> writeSet = new HashMap<>();
    
    StringBuffer log = new StringBuffer("");
    
    // Constructor for Transaction class
    // Recieve transactionID and lastCommittedTransactionNumber
    Transaction(int transactionID, int lastCommittedTransactionNumber) {
        this.transactionID = transactionID;
        this.lastCommittedTransactionNumber = lastCommittedTransactionNumber;
    }
    
    // Get the balance of the account with accountNumber
    public int read (int accountNumber){
        
    }
    
    // Overwrite the newBalance in the account with accountNumber
    public int write (int accountNumber, int newBalance) {
        
    }
    
    // Get the readSet array
    public ArrayList getReadSet(){
        
    }
    
    // Get the writeSet HashMap
    public HashMap getWriteSet(){
        
    }
    
    // Get the transactionID
    public int getTransactionID(){
        
    }
    
    // Get the transactionNumber
    public int getTransactionNumber(){
        
    }
}
