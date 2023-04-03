package transaction.server.transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import transaction.comm.Message;
import transaction.comm.MessageTypes;
import transaction.server.TransactionServer;
import static utils.TerminalColors.ABORT_COLOR;
import static utils.TerminalColors.COMMIT_COLOR;
import static utils.TerminalColors.OPEN_COLOR;
import static utils.TerminalColors.READ_COLOR;
import static utils.TerminalColors.RESET_COLOR;
import static utils.TerminalColors.WRITE_COLOR;

/**
 *
 * @author Usuario
 */
public class TransactionManager implements MessageTypes {
    
    private static int transactionIDCounter = 0;
    
    private static final ArrayList<Transaction> runningTransactions = new ArrayList<>();
    private static final ArrayList<Transaction> abortedTransactions = new ArrayList<>();    
    private static final HashMap<Integer, Transaction> committedTransactions = new HashMap<>();
    
    private static int transactionNumberCounter = 0;
    
    // default constructor
    public TransactionManager() {}
    
    // helper method that returns aborted transactions
    // returns a list of aborted transactions
    public ArrayList<Transaction> getAbortedTransactions() {
        // return the array of aborted transactions
        return abortedTransactions;
    }
    
    // runs a transaction for an incoming client
    public synchronized void runTransaction(Socket client) {
        // start a new transaction for the client
        (new TransactionManagerWorker(client)).start();
    }
    
    // validates a transaction according to OCC
    // with backwards validation
    public boolean validateTransaction(Transaction transaction) {
        // assign variables
        int transactionNumber;
        int lastCommittedTransactionNumber;
        int transactionNumberIndex;
        
        ArrayList<Integer> readSet = transaction.getReadSet();
        HashMap<Integer, Integer> checkedTransactionWriteSet;
        Iterator<Integer> readSetIterator;
        
        Transaction checkedTransaction;
        Integer checkedAccount;
        
        // assign a transaction number for this transaction
        transactionNumber = ++transactionNumberCounter;
        transaction.setTransactionNumber(transactionNumber);
        
        // get the transaction number of the last committed transaction right before this one started
        lastCommittedTransactionNumber = transaction.getLastCommittedTransactionNumber();
        
        //loop through all overlapping transactions
        // start of for loop
        for (transactionNumberIndex = lastCommittedTransactionNumber+1); transactionNumberIndex < transactionnumber; transactionNumberIndex++) {
        
            // get transaction with transaction number
            checkedTransaction = committedTransactions.get(transactionNumberIndex);
            
            // check if transaction with the transaction number was not aborted
            if (checkedTransaction != null) {
            
                // check our own read set against the write set of the checkedTransaction
                checkedTransactionWriteSet = checkedTransaction.getWriteSet();
                
                readSetIterator = readSet.iterator();
                
                // loop while the read set still has an account
                // start of while loop
                while (readSetIterator.hasNext()) {
                
                    // check if an account in the read set is part of the write set in the checkedTransaction
                    checkedAccount = readSetIterator.next();
                    if(checkedTransactionWriteSet.containsKey(checkedAccount)) {
                    
                        // log the conflict of the transaction
                        transaction.log("[TRansactionManager.validateTransaction] Transaction #" + transaction.getTransactionID() +
                                " failed: r/w conflict on account #" + checkedAccount + " with Transaction #" +
                                checkedTransaction.getTransactionID());
                        
                        // return false
                        return false;
                    }
                        
                // end of while loop
                }
            }
            
        // end of for loop
        }
        
        // log the transaction that it does not have any conflicts
        // return true
    }
    
    // writes the write set of a transaction into the operational data
    public void writeTransaction(Transaction transaction) {
        
        HashMap<Integer, Integer> transactionWriteSet = transaction.getWriteSet();
        int account;
        int balance;
        
        // loop through all the entries of this write set
        // start of for loop
        for (Map.Entry<Integer, Integer> entry : transactionWriteSet.entrySet()) {
            account = entry.getKey();
            balance = entry.getValue();
            
            // write this record into operational data
            TransactionServer.accountManager.write(account, balance);
            
            // log that the transaction has been written
            transaction.log("[Transactionmanager.writeTransaction]");
        
        // end of for loop
        }
        
    }
    
    // objects of this inner class run transaction, one thread runs one
    // transaction on behalf of a client
    public class TransactionManagerWorker extends Thread {
        // create network related variables
        Socket client = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        
        // create transaction related variables
        Transaction transaction = null;
        int accountNumber = 0;
        int balance = 0;
        
        // set flag for jumping out of while loop after this transaction closed
        boolean keepgoing = true;
        
        // constructor to open up the network channels
        private TransactionManagerWorker(Socket client) {
            // set up object streams
            this.client = client;
            
            try {
                readFromNet = new ObjectInputStream(client.getInputStream());
                writeToNet = new ObjectOutputStream(client.getOutputStream());
            } catch (IOException e) {
                System.out.println("[Transactionmanager.run] Failed to open object streams");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
    
    // run the transaction
    public void run() {
        // loop until the transaction closes
        // start of while loop
        while (keepgoing) {
        
            // read the message
            try {
                message = (Message) readFromnet.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("[TransactionmanagerWorker.run]");
                System.exit(1);
            }
            
            // process the message
            // switch cases
            switch (message.getType()) {
            
                // process the transaction in the case that it is an
                // open transaction
                case OPEN_TRANSACTION:
                    // assign new transaction ID
                    synchronized (runningTransactions){
                        transaction = new Transaction(++transactionIdCounter, transactionNumberCounter);
                        runningTransactions.add(transaction);
                    }
                    
                    // log what happened to the transaction
                    try {
                        writeToNet.writeObject(transaction.getTransactionID());
                    } catch (IOException e) {
                        System.err.println("[]TransactionManagerWorker.run] error occured");
                    }
                    
                    transaction.log("[TransactionManagerWorker]");
                    break;
                    
                // process the transaction in the case it is a close transaction
                case CLOSE_TRANSACTION:
                    // remove any running transaction
                    synchronized (runningTransactions) {
                    
                    // check if the transaction was validated
                    if (validateTransaction(transaction)) {
                    
                        // add this transaction to the committed transaction
                        committedTransactions.put(transaction.getTransactionnumber(), transaction);
                        // write data to operational data
                        writeTransaction(transaction);
                        
                        // inform the client the transaction was committed
                        try {
                            writeToNet.writeObject((Integer) TRANSACTION_COMMITTED);
                        } catch (IOException e) {
                            System.out.println("[TransactionmanagerWorker.run] CLOSE_TRANSACTION error");
                        }
                        
                        transaction.log("[TransactionmanagerWorker.run] CLOSE_TRANSACTION");
                    }
                    // otherwise, assume the transaction was not validated
                    else {
                        // abort the transaction, tell client the validation failed
                        try {
                            writeToNet.writeObject((Integer) TRANSACTION_ABORTED);
                        } catch (IOException e) {
                            System.out.println("[TransactionmanagerWorker.run] CLOSE_TRANSACTION error when writing transID");
                        }
                        
                        transaction.log("[TransactionmanagerWorker.run] TRANSACTION_ABORTED");
                    }
                }
                        
                // shut down all the network connections
                try {
                    readFromNet.close();
                    writeToNet.close();
                    client.close();
                    
                    keepgoing = false;
                } catch (IOException e) {
                    System.out.println("[TransactionmanagerWorker.run] CLOSE_TRANSACTION error when closing connection");
                }
                // stop the while loop
                    
            }
        }
    }
}
