/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package transaction.server.account;

/**
 *
 * @author Fady
 */
public class AccountManager implements MessageTypes {
    
    // default constructor
    public AccountManager() {}
    
    // helper method that returns accounts still in need of read
    // returns a list of accounts that have yet to be read
    public ArrayList<Account> getReadAccounts() {
        // return the array of accounts needing to be read
    }
    
    // helper method that returns accounts still in need of write
    // returns a list of accounts that have yet to be write
    public ArrayList<Account> getWriteAccounts() {
        // return the array of accounts needing to be write
    }
    
    // Method that writes new balance to the account
    public void setAccountBalance (int accountID, int accountBalance, int balanceOffset) {
        // Read account given by ID
        
        // Check for validation
            // abort if needed
        
        // Add or subract account's balance by offset
        
        // Commit
    }
    
    // Method that reads account details
    public ArrayList<Account> getAccountDetails (int accountID, int accountBalance, int balanceOffset) {
        // Read account given by ID
        
        // Check for validation
            // abort if needed
        
        // Return details of account
    }
    
    // objects of this inner class run transaction, one thread runs one
    // transaction on behalf of a client
    public class AccountManager extends Thread {
        // create network related variables
        
        // create account related variables
        
        // set flag for jumping out of while loop after this account has changed
        
        // constructor to open up the network channels
        private AccountManager(Socket client) {
            // set up object streams
        }
    }
    
    // run the transaction
    public void run() {
        // loop for each account needing to be edited
        // start of while loop
        
            // read the message/command
            
            // process the message/command
            // switch cases
            
                // Perform high-level operations on the account
        
                // Boolean to check if account changes, if write, have been commited
        
            // Validate
        
        // end while loop
    }
}
