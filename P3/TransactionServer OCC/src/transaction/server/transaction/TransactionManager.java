/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package transaction.server.transaction;

/**
 *
 * @author Usuario
 */
public class TransactionManager implements MessageTypes {
    
    // default constructor
    public TransactionManager() {}
    
    // helper method that returns aborted transactions
    // returns a list of aborted transactions
    public ArrayList<Transaction> getAbortedTransactions() {
        // return the array of aborted transactions
    }
    
    // runs a transaction for an incoming client
    public synchronized void runTransaction(Socket client) {
        // start a new transaction for the client
    }
    
    // validates a transaction according to OCC
    // with backwards validation
    public boolean validateTransaction(Transaction transaction) {
        // assign variables
        
        // assign a transaction number for this transaction
        
        // get the transaction number of the last committed transaction right before this one started
        
        //loop through all overlapping transactions
        // start of for loop
        
            // get transaction with transaction number
            
            // check if transaction with the transaction number was not aborted
            
                // check our own read set against the write set of the checkedTransaction
                
                // loop while the read set still has an account
                // start of while loop
                
                    // check if an account in the read set is part of the write set in the checkedTransaction
                    
                        // log the conflict of the transaction
                        
                        // return false
                        
                // end of while loop
            
        // end of for loop
        
        // log the transaction that it does not have any conflicts
        // return true
    }
    
    // writes the write set of a transaction into the operational data
    public void writeTransaction(Transaction transaction) {
        // loop through all the entries of this write set
        // start of for loop
        
            // write this record into operational data
            
            // log that the transaction has been written
        
        // end of for loop
        
    }
    
    // objects of this inner class run transaction, one thread runs one
    // transaction on behalf of a client
    public class TransactionManagerWorker extends Thread {
        // create network related variables
        
        // create transaction related variables
        
        // set flag for jumping out of while loop after this transaction closed
        
        // constructor to open up the network channels
        private TransactionManagerWorker(Socket client) {
            // set up object streams
        }
    }
    
    // run the transaction
    public void run() {
        // loop until the transaction closes
        // start of while loop
        
            // read the message
            
            // process the message
            // switch cases
            
                // process the transaction in the case that it is an
                // open transaction
                
                    // assign new transaction ID
                    
                    // log what happened to the transaction
                    
                // process the transaction in the case it is a close transaction
                
                    // remove any running transaction
                    
                    // check if the transaction was validated
                    
                        // add this transaction to the committed transaction
                        
                        // write data to operational data
                        
                        // inform the client the transaction was committed
                        
                    // otherwise, assume the transaction was not validated
                    
                        // abort the transaction, tell client the validation failed
                    
                // shut down all the network connections
                
                // stop the while loop
    }
}
