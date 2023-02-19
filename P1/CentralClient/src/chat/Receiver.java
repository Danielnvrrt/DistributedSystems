package chat;
import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.PropertyHandler;
import java.util.Properties;

import utils.NetworkUtilities;

public class Receiver extends Thread {
  static ServerSocket receiverSocket = null;
  static String userName = null;

  /**
   * Constructor
   */
  public Receiver() {
	  
	// try making a receiver socket
    try {
      receiverSocket = new ServerSocket(ChatClient.myNodeInfo.getPort());
      System.out.println("[Receiver.Receiver] receiver socket created, listening on port " + ChatClient.myNodeInfo.getPort());
    }
    
    // if receiver socket failed to be created
    catch (IOException ex) {
      Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, "Creating receiver socket failed", ex);
    }

    // output that a client is listening on the server
    System.out.println(ChatClient.myNodeInfo.getName() + " listening on " + ChatClient.myNodeInfo.getAddress() + ":"
        + ChatClient.myNodeInfo.getPort());
  }

  @Override
  public void run() {
    while (true) {
    	
      // try having receiver accepted by server
      try
      {
        (new ReceiverWorker(receiverSocket.accept())).start();
      }
      
      // if server does not accept receiver
      catch (IOException e)
      {
        System.err.println("[Receiver.run] Warning: Error accepting client");
      }
    }
  }
}
