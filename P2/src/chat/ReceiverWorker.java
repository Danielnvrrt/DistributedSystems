package chat;

import java.net.ServerSocket;

import java.net.SocketException;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import message.Message;
import message.MessageTypes;
import org.w3c.dom.Node;
import utils.PropertyHandler;
import message.MessageTypes;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import chat.Sender;

import javax.sound.midi.SysexMessage;

public class ReceiverWorker extends Thread implements MessageTypes{
	Socket serverConnection = null;
    ObjectInputStream readFromNet = null;
    ObjectOutputStream writeToNet = null;


	public ReceiverWorker(Socket sock) {
		this.serverConnection = sock;
	}

  @SuppressWarnings("removal")
@Override
  public void run() {
	Message message = null;
	  
	try
	{
       writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());
       readFromNet = new ObjectInputStream(serverConnection.getInputStream());

       message = (Message) readFromNet.readObject();
       
       serverConnection.close();
       
	}
	catch(IOException | ClassNotFoundException ex)
	{
		Logger.getLogger(ReceiverWorker.class.getName()).log(Level.SEVERE, "[ReceiverWorker.run] Could not open object streams.", ex);
	}
    switch (message.getType()) {
	  //Join Method
      case JOIN:
		// Get list of participants
    	ArrayList<NodeInfo> clientsInfo = (ArrayList<NodeInfo>) message.getContent();
    	ChatClient.participantsInfo.get(0).setJoined(true);
    	
    	// Add participants to client's own list
    	for (int count = 0; count < clientsInfo.size(); count++) {
    		if (clientsInfo.get(count).getAddress() != ChatClient.participantsInfo.get(0).getAddress()
    			&& clientsInfo.get(count).getPort() != ChatClient.participantsInfo.get(0).getPort() 
    			&& !ChatClient.currentParticipants.contains(clientsInfo.get(count).getAddress() + String.valueOf(clientsInfo.get(count).getPort()))) {
    				System.out.println(clientsInfo.get(count).getName() + " Joined.");
    				if(!ChatClient.visitedParticipant.contains(clientsInfo.get(count).getAddress() + String.valueOf(clientsInfo.get(count).getPort()))) {
    					clientsInfo.get(count).setJoined(false);
    				}
    				else {
    					clientsInfo.get(count).setJoined(true);
    				}
    				ChatClient.participantsInfo.add(clientsInfo.get(count));
    				ChatClient.currentParticipants.add(clientsInfo.get(count).getAddress() + String.valueOf(clientsInfo.get(count).getPort()));
    		}
    	}
    	
		// Join each new participant
    	for (int count = 1; count < ChatClient.participantsInfo.size(); count++) {

			if(ChatClient.participantsInfo.get(count).getJoined() == false) {
				(ChatClient.sender[0] = new Sender("JOIN " + ChatClient.participantsInfo.get(count).getAddress() + " " + String.valueOf(ChatClient.participantsInfo.get(count).getPort()), count)).start();
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ChatClient.participantsInfo.get(count).setJoined(true);
			}
		}

        break;
	  // Shutdown and Leave methods
	  case SHUTDOWN:
	  case LEAVE:
		  // Receive leaving client information
		  NodeInfo leavingClient = (NodeInfo) message.getContent();
		  System.out.println(leavingClient.getName() + " left chat");
		  // Remove client from all lists
		  ChatClient.currentParticipants.remove(leavingClient.getAddress() + String.valueOf(leavingClient.getPort()));
		  ChatClient.visitedParticipant.remove(leavingClient.getAddress() + String.valueOf(leavingClient.getPort()));
		  for(int i = 0; i < ChatClient.participantsInfo.size(); i++){
			  if(leavingClient.getName() == ChatClient.participantsInfo.get(i).getName()){
				  ChatClient.participantsInfo.remove(ChatClient.participantsInfo.get(i));
			  }
		  }
		  break;

	  // Shutdown all method
	  case SHUTDOWN_ALL:
		  // Receive the client information who sent the shutdown_all request
		  NodeInfo shutdownClient = (NodeInfo) message.getContent();
		  System.out.println("Received shutdown message from " + shutdownClient.getName() +", exiting");
		  // Close server connection
		  try {
			  serverConnection.close();
		  } catch (IOException ex) {
			  // dont care
		  }
		  // Stop system
		  System.exit(0);
		  break;
      case NOTE:
		// Display the message
        System.out.println((String) message.getContent());

        break;

      default:
        // cannot happen
    }
  }
}
