import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import chat.ChatClient;

public class ChatServer extends ChatClient implements Runnable {

	
	
	
    @Override
    public void run() {
        try
        {
            ServerSocket server = new ServerSocket(9999);
            Socket client = server.accept();
        }
        catch (IOException e)
        {
        	Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "[ChatServer.run] Could not open the server.", e);
			System.exit(1);
        }
    }
}
