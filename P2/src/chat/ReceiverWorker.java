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
import java.util.Properties;
import chat.Sender;

public class ReceiverWorker extends Receiver{
	Socket serverConnection = null;
    ObjectInputStream readFromNet = null;
    ObjectOutputStream writeToNet = null;


	public ReceiverWorker(Socket sock) {
		this.serverConnection = sock;
		
		
		try
		{
			readFromNet = new ObjectInputStream(serverConnection.getInputStream());
			writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());
		}
		catch(IOException ex)
		{
			Logger.getLogger(ReceiverWorker.class.getName()).log(Level.SEVERE, "[ReceiverWorker.run] Could not open object streams.", ex);
		}
        System.out.println("Aqui llego");
	}

  @Override
  public void run() {
	Message message = null;
	try {
		ObjectInputStream readFromNet = new ObjectInputStream(serverConnection.getInputStream());
		message  = (Message) readFromNet.readObject();
    }

    catch (IOException | ClassNotFoundException ex) {
      Logger.getLogger(ReceiverWorker.class.getName()).log(Level.SEVERE,
          "[ReceiverWorker.run] Message could not be read", ex);

      System.exit(1);
    }

    switch (message.getType()) {
      case JOIN:
        System.out.println("holaaaa");
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

        try {
          serverConnection.close();
        } catch (IOException ex) {
          Logger.getLogger(ReceiverWorker.class.getName()).log(Level.SEVERE, "Command NOTE failed", ex);
        }

        break;

      default:
        // cannot happen
    }
  }
}
