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
      case JOIN:
    	@SuppressWarnings("unchecked")
    	ArrayList<NodeInfo> clientsInfo = (ArrayList<NodeInfo>) message.getContent();
    	ChatClient.participantsInfo.get(0).setJoined(true);
    	
    	// combine two participants lists
    	for (int count = 0; count < clientsInfo.size(); count++) {
    		if (clientsInfo.get(count).getAddress() != ChatClient.participantsInfo.get(0).getAddress()
    			&& clientsInfo.get(count).getPort() != ChatClient.participantsInfo.get(0).getPort()) {
    				System.out.println(clientsInfo.get(count).getName() + " Joined.");
    				if(!ChatClient.visitedParticipant.contains(clientsInfo.get(count).getAddress() + String.valueOf(clientsInfo.get(count).getPort()))) {
    					clientsInfo.get(count).setJoined(false);
    				}
    				else {
    					clientsInfo.get(count).setJoined(true);
    				}
    				ChatClient.participantsInfo.add(clientsInfo.get(count));
    		}
    	}
        break;
      case SHUTDOWN:
        System.out.println("Received shutdown message from server, exiting");

        try {
          serverConnection.close();
        } catch (IOException ex) {
          // dont care
        }

        System.exit(0);

        break;

      case NOTE:

        System.out.println((String) message.getContent());

        break;

      default:
        // cannot happen
    }
  }
}
