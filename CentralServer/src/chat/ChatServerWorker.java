package chat;


import java.util.Iterator;
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
public class ChatServerWorker extends Thread implements MessageTypes{

    Socket chatConnection = null;
    ObjectOutputStream writeToNet = null;
    ObjectInputStream readFromNet = null;

    public ChatServerWorker(Socket sock){
        this.chatConnection = sock;
    }

    @Override
    public void run() {
        NodeInfo participantInfo = null;
        Iterator<NodeInfo> participantsIterator;
        Message message = null;

        try{
            // get object streams
            writeToNet = new ObjectOutputStream(chatConnection.getOutputStream());
            readFromNet = new ObjectInputStream(chatConnection.getInputStream());

            // read message
            message = (Message) readFromNet.readObject();

            chatConnection.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println("[chat.ChatServerWorker.run] Failed to open object stream");

            System.exit(1);
        }

        //processing message
        switch (message.getType())
        {
            case JOIN:

                // read participant's NodeInfo
                NodeInfo joiningParticipantNodeInfo = (NodeInfo) message.getContent();

                // add this client to list of participants
                ChatServer.participants.add(joiningParticipantNodeInfo);

                // show who joined
                System.out.print("\n" + joiningParticipantNodeInfo.getName() + " joined.");

                // show all particiants
                System.out.print(" All current participants: ");
                for(NodeInfo p : ChatServer.participants){
                    System.out.print(p.getName() + ", ");
                }
                System.out.println();
                break;
            case LEAVE:

                // read participant's NodeInfo
                NodeInfo leavingParticipantNodeInfo = (NodeInfo) message.getContent();

                // remove client from list of participants
                //ChatServer.participants.remove(leavingParticipantNodeInfo);


                for(NodeInfo p : ChatServer.participants){
                    if(p.getName().equals(leavingParticipantNodeInfo.getName())){
                        ChatServer.participants.remove(p);
                        break;
                    }
                }
                //ChatServer.participants.remove(index);

                // show who left
                System.out.print("\n" + leavingParticipantNodeInfo.getName() + " left.");

                // show all participants
                System.out.print(" All current participants: ");
                for(NodeInfo p : ChatServer.participants){
                    System.out.print(p.getName() + ", ");
                }
                System.out.println();

                break;
            case NOTE:

                // print message
                System.out.println(message.getContent());

                participantsIterator = ChatServer.participants.iterator();
                while (participantsIterator.hasNext())
                {
                    // get next participant

                    participantInfo = participantsIterator.next();
                    try
                    {
                        // open connection to client
                        chatConnection = new Socket(participantInfo.getAddress(), participantInfo.getPort());
                        System.out.println(participantInfo.getAddress());

                        // open object streams
                        writeToNet = new ObjectOutputStream(chatConnection.getOutputStream());
                        readFromNet = new ObjectInputStream(chatConnection.getInputStream());

                        // send shutdown message
                        writeToNet.writeObject(new Message(NOTE, message.getContent()));

                        // close connection
                        chatConnection.close();
                    }
                    catch (IOException ex) {
                        // TODO: handle exception
                    }
                }


                break;

            case SHUTDOWN:
                // remove this participant's info
                NodeInfo leavingParticipantInfo = (NodeInfo) message.getContent();
                for(NodeInfo p : ChatServer.participants){
                    if(p.getName().equals(leavingParticipantInfo.getName())){
                        ChatServer.participants.remove(p);
                        break;
                    }
                }

                // show who left
                System.out.print(leavingParticipantInfo.getName() + " left. Remaining participants: ");

                // print out all remaining participants
                // show all participants
                for(NodeInfo p : ChatServer.participants){
                    System.out.print(p.getName() + ", ");
                }
                System.out.println();
                break;

            case SHUTDOWN_ALL:
                // run through all the participants and shut down each single one
                participantsIterator = ChatServer.participants.iterator();
                while (participantsIterator.hasNext())
                {
                    // get next participant

                    participantInfo = participantsIterator.next();
                    try
                    {
                        // open connection to client
                        chatConnection = new Socket(participantInfo.getAddress(), participantInfo.getPort());
                        System.out.println(participantInfo.getAddress());

                        // open object streams
                        writeToNet = new ObjectOutputStream(chatConnection.getOutputStream());
                        readFromNet = new ObjectInputStream(chatConnection.getInputStream());

                        // send shutdown message
                        writeToNet.writeObject(new Message(SHUTDOWN, null));

                        // close connection
                        chatConnection.close();
                    }
                    catch (IOException ex) {
                        // TODO: handle exception
                    }
                }


                break;
        }
    }
}
