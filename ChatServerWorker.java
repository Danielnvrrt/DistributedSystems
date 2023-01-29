import java.io.ObjectOutputStream;
import java.util.Iterator;

import chat.NodeInfo;
import message.Message;

public class ChatServerWorker {

	@Override
    public void run() {
        NodeInfo participantInfo = null;
        Iterator<E> <NodeInfo> participantsIterator;

        try{
            // get object streams
            writeToNet = new ObjectOUtputStream(chatConnection.getOutputStream());
            readFromNet = new ObjectInputStream(chatConnection.getInputStream());

            // read message
            message = (Message) readFromNet.readObject();

            chatConnection.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println("[ChatServerWorker.run] Failed to open object stream");

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
            System.out.print(joiningParticipantNodeInfo.getName() + " joined. All current participants: ");

            // print out all current participants
        case LEAVE:
        case SHUTDOWN:
            // remove this participant's info
            NodeInfo leavingParticipantInfo = (NodeInfo) message.getContent();
            if (ChatServer.participants.remove(leavingParticipantInfo))
            {
                System.err.println(leavingParticipantInfo.getName() + " removed");
            }
            else
            {
                System.err.println(leavingParticipantInfo.getName() + " not found");
            }

            // show who left
            System.out.print(leavingParticipantInfo.getName() + " left. Remaining participants: ");

            // print out all remaining participants
            participantsIterator = ChatServer.participants.iterator();
            while(participantsIterator.hasNext())
            {
                participantInfo = participantsIterator.next();
                System.out.print(participantInfo.name + " ");
            }
            System.out.println();
            break;
        
        case SHUTDOWN_ALL:
            // run through all the participants and shut down each single one
            participantsIterator = ChatServer.participants.iterator;
            while (participantsIterator.hasNext())
            {
                // get next participant
                participantInfo = participantsIterator.next();

                try
                {
                    // open connection to client
                    chatConnection = new Socket(participantInfo.adress, participantInfo.port);

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
    }
    }
}
