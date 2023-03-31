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
        Integer balance;
        
        // check if value to be read was written by this transaction
        balance = writeSet.get(accountNumber);
        
        // if not, read the committed version of it
        if(balance == null){
            balace = TransactionServer.accountManager.read(accountNumber);
        }
        
        if(!readSet.contains(accountNumber)){
            readSet.add(accountNumber);
        }
        
        return balance;
    }   
    
    
    // Overwrite the newBalance in the account with accountNumber
    public int write (int accountNumber, int newBalance) {
        int oldBalance = read(accountNumber);
        
        if (!writeSet.containsKey(accountNumber)){
            writeSet.put(accountNumber, newBalance);
        }
        
        return oldBalance;
    }
    
    // Get the readSet array
    public ArrayList getReadSet(){
        return readSet;
    }
    
    // Get the writeSet HashMap
    public HashMap getWriteSet(){
        return writeSet;
    }
    
    // Get the transactionID
    public int getTransactionID(){
        return transactionID;
    }
    
    // Get the transactionNumber
    public int getTransactionNumber(){
        return transactionNumber;
    }
}
