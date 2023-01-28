package chat;

@Override
public void run(){
    NodeInfo participantInfo = null;
    Iterator <NodeInfo> participantsIterator;

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
}