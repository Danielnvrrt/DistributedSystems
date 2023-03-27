/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package transaction.client;

/**
 *
 * @author Usuario
 */
public class TransactionServerProxy implements MessageTypes {
    
    /*
    *Constructor for serve proxy
    *
    */
    TransactionServerProxy(String host, int port){
        // contructs the server using the given information
    }
    
    //opens a transaction
    public int openTransaction() {
        // this is where we will create the transaction
        // that will start a connection and also record the message
        
        // the transaction will return the transaction id
    }
    
    // closes a transaction
    // it will request this transaction to close
    public int closeTransaction() {
        // records the message in the transaction
        // return that the transaction has been committed
    }
    
    //reads a value from an account
    public int read(int accountNumber) throws TransactionAbortedException {
        // create message variable
        // read the content
        // return the content of the message or abort
    }
    
    // writes a value to an account
    public void write(int accountNumber, int amount) throws TransactionAbortedException {
        // create message and content variables
        // write the content
        // throw exception if aborted
    }
}
