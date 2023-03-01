package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import java.util.logging.Level;
import java.util.logging.Logger;

import message.Message;
import message.MessageTypes;

public class Sender extends Thread implements MessageTypes {

    static Scanner userInput = new Scanner(System.in);
    String inputLine = null;
    String forceCommand = null;
    Socket serverConnection = null;
    Integer participantNum = 0;
    boolean hasJoined = false;

    /**
     * Constructor
     */
    public Sender() {
        userInput = new Scanner(System.in);
    }
    
    public Sender(String command, Integer count) {
    	participantNum = count;
    	inputLine = command;
    	userInput = new Scanner(System.in);
    }

    /**
     * Implementation interface Runnable not needed for threading
     */
    @Override
    public void run()
    {
        ObjectOutputStream writeToNet;
        ObjectInputStream readFromNet;

        // until forever, unless the user enters SHUTDOWN or SHUTDONW ALL
        while (true)
        {
            //get user input or force a command
        	if (inputLine == null) {
        		inputLine = userInput.nextLine();
        	}


            if (inputLine.startsWith("JOIN"))
            {

            	
            	// Check if client is already connected
                if (hasJoined == true)
                {
                	if (participantNum == 0) {
                		System.err.println("You have already joined a chat ...");
                	}
                	inputLine = null;
                    continue;
                }
				
                
                // read server information user provided with JOIN command
                String[] connectivityInfo = null;
                connectivityInfo = inputLine.split("[ ]+");

                // if there is information, that may override the connectivity information // that was provided through the properties


                try
                { 
                	ChatClient.serverNodeInfo = new NodeInfo(connectivityInfo[1], Integer.parseInt(connectivityInfo[2])); 
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    // don't do anything, we may have defaults

                }
                
                

                // check if we have valid server connectivity information
                if (ChatClient.serverNodeInfo == null)
                {
                    System.err.println("[Sender].run No server connectivity information provided!");
                    continue;
                }
                
                ChatClient.visitedParticipant.add(connectivityInfo[1] + connectivityInfo[2]);

                // server information was provided, so send join request
                try
                {
                	
                    serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());
                    // open object streams
                    

                    readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());


                    writeToNet.writeObject(new Message(JOIN, ChatClient.participantsInfo));


                    // close connection
                    serverConnection.close();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, "Error connecting to server or opening or writing/reading object streams or closing connection", ex);
                    continue;
                }

                // we are in!
                hasJoined = true;
                System.out.println("Joined chat ...");
                forceCommand = null;
            }

            else if (inputLine.startsWith("LEAVE"))
            {
                if (ChatClient.participantsInfo.get(participantNum).getJoined() == false)
                {
                    System.err.println("You have not joined a chat yet ...");
                    continue;
                }

                // send leave request
                try
                {
                    // open connection to server
                    serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());
                    
                    // open object streams
                    readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    // send join request
                    writeToNet.writeObject(new Message(LEAVE, ChatClient.myNodeInfo));

                    // close connection
                    serverConnection.close();

                }
                catch (IOException ex)
                {
                    Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, "Error connecting to server or opening or writing/reading object streams or closing connection", ex);
                    continue;
                }

                // we are out
                ChatClient.participantsInfo.get(participantNum).hasJoined = false;
                System.out.println("Left chat ...");
            }
            else if (inputLine.startsWith("SHUTDOWN_ALL"))
            {
                if (ChatClient.participantsInfo.get(0).hasJoined == false)
                {
                    System.err.println("To shutdown the whole chat, you need to first Join a chat ...");
                    continue;
                }

                // we are a participant, send out a SHUTDOWN_ALL message
                try
                {
                    // open connection to server
                    serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());
                    
                    // open object streams
                    readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                    writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                    // send shutdown all request
                    writeToNet.writeObject(new Message(SHUTDOWN, ChatClient.myNodeInfo));

                    // close connection
                    serverConnection.close();

                }
                catch (IOException ex)
                {
                    Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, "Error connecting to server or opening or writing/reading object streams or closing connection", ex);
                    continue;
                }
                System.out.println("Sent shutdown all request ... \n");
            }
            else if (inputLine.startsWith("SHUTDOWN"))
            {
                // if we are a participant, leave chat first  
                if (ChatClient.participantsInfo.get(0).hasJoined == true)
                {                  
                    try
                    {
                        // open connection to server
                        serverConnection = new Socket(ChatClient.serverNodeInfo.getAddress(), ChatClient.serverNodeInfo.getPort());
                    
                        // open object streams
                        readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                        writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                        // send leave request
                        writeToNet.writeObject(new Message(SHUTDOWN, ChatClient.myNodeInfo));

                        // close connection
                        serverConnection.close();

                        System.out.println("Left chat ...");

                    }
                    catch (IOException ex)
                    {
                        Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, "Error connecting to server or opening or writing/reading object streams or closing connection", ex);
                        continue;
                    }
             
                }
                System.out.println("Exiting ... \n");
                System.exit(0);
            }

            else
            {
                if (ChatClient.participantsInfo.get(0).hasJoined == false)
                {
                    System.err.println("You need to join a chat first!");
                    inputLine = null;
                    continue;
                }

                // send note
                try
                {
                    // open connection to server
                	for (int count = 1; count < ChatClient.participantsInfo.size(); count++) {
                		serverConnection = new Socket(ChatClient.participantsInfo.get(count).getAddress(), ChatClient.participantsInfo.get(count).getPort());
                        
                        // open object streams
                        readFromNet = new ObjectInputStream(serverConnection.getInputStream());
                        writeToNet = new ObjectOutputStream(serverConnection.getOutputStream());

                        // send note request
                        writeToNet.writeObject(new Message(NOTE, ChatClient.myNodeInfo.getName() + ": " + inputLine));
                        writeToNet.flush();

                        // close connection
                        serverConnection.close();
        			}
                	System.out.println(ChatClient.myNodeInfo.getName() + ": " + inputLine);
                    inputLine = null;

                }
                catch (IOException ex)
                {
                    Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, "Sending message error", ex);
                    continue;
                }
            }
            
        }
  
    }
}