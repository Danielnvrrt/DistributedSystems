package appserver.satellite;

import appserver.job.Job;
import appserver.comm.ConnectivityInfo;
import appserver.job.UnknownToolException;
import appserver.server.SatelliteManager;
import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.job.Tool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.PropertyHandler;

/**
 * Class [Satellite] Instances of this class represent computing nodes that execute jobs by
 * calling the callback method of tool a implementation, loading the tool's code dynamically over a network
 * or locally from the cache, if a tool got executed before.
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Satellite extends Thread {

    private ConnectivityInfo satelliteInfo = new ConnectivityInfo();
    private ConnectivityInfo serverInfo = new ConnectivityInfo();
    private HTTPClassLoader classLoader = null;
    private Hashtable toolsCache = null;

    public Satellite(String satellitePropertiesFile, String classLoaderPropertiesFile, String serverPropertiesFile) {

        // read this satellite's properties and populate satelliteInfo object,
        // which later on will be sent to the server
        // ...
    	Properties properties = null;
    	try {
			properties = new PropertyHandler(satellitePropertiesFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	// System.out.println(properties);
    	satelliteInfo.setPort(Integer.parseInt(properties.getProperty("PORT")));
    	satelliteInfo.setName(properties.getProperty("NAME"));
        
        
        // read properties of the application server and populate serverInfo object
        // other than satellites, the as doesn't have a human-readable name, so leave it out
        // ...
    	try {
			properties = new PropertyHandler(serverPropertiesFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	serverInfo.setPort(Integer.parseInt(properties.getProperty("PORT")));
    	serverInfo.setHost(properties.getProperty("HOST"));
        
        
        // read properties of the code server and create class loader
        // -------------------
        // ...
    	try {
			properties = new PropertyHandler(classLoaderPropertiesFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	classLoader = new HTTPClassLoader(properties.getProperty("HOST"), Integer.parseInt(properties.getProperty("PORT")));

        
        // create tools cache
        // -------------------
        // ...
    	
        
    }

    @Override
    public void run() {

		// register this satellite with the SatelliteManager on the server
        // ---------------------------------------------------------------
        // ...
    	SatelliteManager satelliteManager = new SatelliteManager();
    	satelliteManager.registerSatellite(satelliteInfo);
        
        
        // create server socket
        // ---------------------------------------------------------------
        // ...
    	Socket serversocket = null;
    	try {
			serversocket = new Socket(satelliteInfo.getHost(), satelliteInfo.getPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        // start taking job requests in a server loop
        // ---------------------------------------------------------------
        // ...
    	while(true) {
			String satellitePropertiesFile = "C:\\Users\\user\\Downloads\\Application Server Skeleton\\config\\Satellite.Earth.properties";
    		String classLoaderPropertiesFile = "C:\\Users\\user\\Downloads\\Application Server Skeleton\\config\\WebServer.properties";
    		String serverPropertiesFile = "C:\\Users\\user\\Downloads\\Application Server Skeleton\\config\\Server.properties";
    		Satellite satellite = new Satellite(satellitePropertiesFile, classLoaderPropertiesFile, serverPropertiesFile);
			SatelliteThread satellitethread = new SatelliteThread(serversocket, satellite);
    	}
    	
    }

    // inner helper class that is instanciated in above server loop and processes single job requests
    private class SatelliteThread extends Thread {

        Satellite satellite = null;
        Socket jobRequest = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;

        SatelliteThread(Socket jobRequest, Satellite satellite) {
            this.jobRequest = jobRequest;
            this.satellite = satellite;
        }

        @Override
        public void run() {
            // setting up object streams
            // ...
        	ObjectInputStream readFromNet = null;
    		Integer result = null;
			try {
				readFromNet = new ObjectInputStream(jobRequest.getInputStream());
				result = (Integer) readFromNet.readObject();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
            
            // reading message
            // ...
			System.out.println(result);
            
            switch (message.getType()) {
                case JOB_REQUEST:
                    // processing job request
                    // ...
                	System.out.println("Job has been processed");
                    break;

                default:
                    System.err.println("[SatelliteThread.run] Warning: Message type not implemented");
            }
        }
    }

    /**
     * Aux method to get a tool object, given the fully qualified class string
     * If the tool has been used before, it is returned immediately out of the cache,
     * otherwise it is loaded dynamically
     */
    public Tool getToolObject(String toolClassString) throws UnknownToolException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        Tool toolObject = null;

        // ...
        
        return toolObject;
    }

    public static void main(String[] args) {
        // start the satellite
    	Satellite satellite = null;
    	if(args.length > 0) {
    		satellite = new Satellite(args[0], args[1], args[2]);
    	} else {
    		String satellitePropertiesFile = "C:\\Users\\user\\Downloads\\Application Server Skeleton\\config\\Satellite.Earth.properties";
    		String classLoaderPropertiesFile = "C:\\Users\\user\\Downloads\\Application Server Skeleton\\config\\WebServer.properties";
    		String serverPropertiesFile = "C:\\Users\\user\\Downloads\\Application Server Skeleton\\config\\Server.properties";
    		satellite = new Satellite(satellitePropertiesFile, classLoaderPropertiesFile, serverPropertiesFile);
    	}
        satellite.run();
    }
}
