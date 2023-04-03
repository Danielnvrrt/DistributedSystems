/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package transaction.server.transaction;

import transaction.server.TransactionServer;
import java.util.ArrayList;
import java.util.HashMap;
package transaction.server.account;

/**
 *
 * @author Fady
 */
public class Account {
    int accountID;
    int accountBalance;
    int lastCommittedAccountID;
    
    ArrayList<Integer> readSet = new ArrayList<>();
    HashMap<Integer, Integer> writeSet = new HashMap<>();
    
    StringBuffer log = new StringBuffer("");
    
    // Constructor for Transaction class
    // Recieve transactionID and lastCommittedTransactionNumber
    Account(int accountID, int accountBalance, int lastCommittedAccountID) {
        this.accountID = accountID;
        this.accountBalance = accountBalance;
        this.lastCommittedAccountID = lastCommittedAccountID;
    }
    
    public int readAcc(accountID) {
        
    }
    
    public int writeAcc(accountID, accountBalance) {
        
    }
    
    public int getAccountID(){
        return accountID;
    }
    
    public int getAccountBalance(accountID) {
        return accountBalance;
    }
    
    
}
