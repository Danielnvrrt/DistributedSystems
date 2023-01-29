import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
 

public class ChatServer extends ChatClient implements Runnable
{
	
	@Override
	public void run()
	{
		try
		{
			ServerSocket server = new ServerSocket(9999);
			Socket client = server.accept();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
