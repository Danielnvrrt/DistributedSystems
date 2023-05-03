package appserver.server;

import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.comm.ConnectivityInfo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import utils.PropertyHandler;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Server {

    // Singleton objects - there is only one of them. For simplicity, this is not enforced though ...
    static SatelliteManager satelliteManager = null;
    static LoadManager loadManager = null;
    static ServerSocket serverSocket = null;

    public Server(String serverPropertiesFile) {

        // create satellite manager and load manager
        // ...
        satelliteManager = new SatelliteManager();
        loadManager = new LoadManager();
        // read server properties and create server socket
        // ...
        Properties properties = null;
        try {
            properties = new PropertyHandler(serverPropertiesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }   
        int port = Integer.parseInt(properties.getProperty("PORT"));
        try {
            serverSocket = new ServerSocket(port);
	} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}  
    }

    public void run() {
    // serve clients in server loop ...
    // when a request comes in, a ServerThread object is spawned
    // ...
        while(true) {
            Socket client = serverSocket.accept();
            ServerThread serverThread = new ServerThread(client);
            // Do we have to start it in a new thread?
            serverThread.start();
        }
    }

    // objects of this helper class communicate with satellites or clients
    private class ServerThread extends Thread {

        Socket client = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;

        private ServerThread(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            // set up object streams and read message
            // ...
		ObjectInputStream readFromNet = null;
                ObjectOutputStream writeToNet = null;
                Message message = null;
			try {
                            readFromNet = new ObjectInputStream(client.getInputStream());
                            writeToNet = new ObjectOutputStream(client.getOutputStream());
                            message = (Message) readFromNet.readObject();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            
            // process message
            switch (message.getType()) {
                case REGISTER_SATELLITE:
                    // read satellite info
                    // ...
			System.out.println(message.getContent());
                    
                    // register satellite
                    synchronized (Server.satelliteManager) {
                        // ...
			    Server.satelliteManager.registerSatellite(message.getContent());
                    }

                    // add satellite to loadManager
                    synchronized (Server.loadManager) {
                        // ...
			    Server.loadManager.satelliteAdded(message.getContent());
                    }

                    break;

                case JOB_REQUEST:
                    System.err.println("\n[ServerThread.run] Received job request");
                    String satelliteName = null;
			 ConnectivityInfo satelliteConInfo = null;
                    synchronized (Server.loadManager) {
                        // get next satellite from load manager
                        // ...
			    satelliteName = Server.loadManager.nextSatellite();
                        
                        // get connectivity info for next satellite from satellite manager
                        // ...
			    satelliteConInfo = Server.satelliteManager.getSatelliteForName(satelliteName);
                    }

                    Socket satellite = null;
                    // connect to satellite
                    // ...
			satellite = new Socket(satelliteConInfo.getHost(), satelliteConInfo.getPort());

                    // open object streams,
                    // forward message (as is) to satellite,
                    // receive result from satellite and
                    // write result back to client
                    // ...
			ObjectOutputStream writeToNet = new ObjectOutputStream(satellite.getOutputStream());
            		writeToNet.writeObject(message);
            
            		
            		ObjectInputStream readFromNet = new ObjectInputStream(satellite.getInputStream());
            		Integer result = (Integer) readFromNet.readObject();
            		System.out.println("RESULT: " + result);
			    
			ObjectOutputStream writeToNet = new ObjectOutputStream(client.getOutputStream());
            		writeToNet.writeObject(readFromNet.readObject());

                    break;

                default:
                    System.err.println("[ServerThread.run] Warning: Message type not implemented");
            }
        }
    }

    // main()
    public static void main(String[] args) {
        // start the application server
        Server server = null;
        if(args.length == 1) {
            server = new Server(args[0]);
        } else {
            server = new Server("../../config/Server.properties");
        }
        server.run();
    }
}
