
public class ReceiverWorker extends Receiver
{
  




  @Override
  public void run()
  {
    try
    {
      message = (Message)readFromNet.readObject();
   }
  
   catch (IOException | ClassNotFoundException ex)
    {
      Logger.getLogger(ReceiverWorker.class.getName()).log(level.SEVERE, "[ReceiverWorker.run] Message could not be read", ex);
    
      System.exit(1);
   }
  
    switch (message.getType())
    {
      case SHUTDOWN:
        System.out.println("Received shutdown message from server, exiting");
      
        try
        {
          serverConnection.close();
        }
       catch (IOException ex)
       {
         //dont care
       }
      
       System.exit(0);
      
       break;
      
     case NOTE:
      
       System.out.println((String) message.getContent());
      
        try
        {
          serverConnection.close();
        }
        catch (IOException ex)
        {
          Logger.getLogger(ReceiverWorker.class.getName()).log(level.SEVERE, "Command NOTE failed", ex);
        }
      
       break;
      
     default:
        // cannot happen
     }
   }
}
