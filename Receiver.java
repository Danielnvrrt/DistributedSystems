public class Receiver extends Thread
{
  static ServerSocket receiverSocket = null
  static String userName = null;
  
  /**
   * Constructor
   */
  public Receiver()
  {
    try
    {
      receiverSocket = new serverSocket(ChatClient.myNodeInfo.getPort());
      System.out.println("[Receiver.Receiver] receiver socket created, listening on port " + ChatClient.myNodeInfo.getPort());
    }
    catch (IOException ex)
    {
      Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, "Creating receiver socket failed", ex);
    }
    
    System.out.println(ChatClient.myNodeInfo.getName() + " listening on " + ChatClient.myNodeInfo.getAddress() + ":" + ChatClient.myNodeInfo.getPort();
  }
  
  @Override
  public void run()
  {
    while(true)
    {
      try
      {
        (new ReceiverWorker(receiverSocket.accept())).start();
      }
      catch (IOException e)
      {
        System.err.println("[Receiver.run] Warning: Error accepting client");
      }
    }
  }
}
