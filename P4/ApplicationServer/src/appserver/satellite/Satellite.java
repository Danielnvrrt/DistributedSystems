package appserver.satellite;

import appserver.job.Job;
import appserver.comm.ConnectivityInfo;
import appserver.job.UnknownToolException;
import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
//import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.job.Tool;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.lang.reflect.InvocationTargetException;
import java.lang.*;
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
        String localIpAddress = null;
        try {
            // get local IP addres
            InetAddress localhost = InetAddress.getLocalHost();
            localIpAddress = localhost.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        satelliteInfo.setHost(localIpAddress);
        
        
        // read properties of the application server and populate serverInfo object
        // other than satellites, the as doesn't have a human-readable name, so leave it out
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
        if (classLoader != null) {
            System.err.println("[Satellite.Satellite] HTTPClassLoader created on " + satelliteInfo.getName());
        } else {
            System.err.println("[Satellite.Satellite] Could not create HTTPClassLoader, exiting ...");
            System.exit(1);
        }
        
        // create tools cache
        // -------------------
        // ...
        toolsCache = new Hashtable();
    }

    @Override
    public void run() {

		// register this satellite with the SatelliteManager on the server
        // ---------------------------------------------------------------
        // ... 	
        
        
        // create server socket
        // ---------------------------------------------------------------
        // ...
    	ServerSocket serverSocket = null;
    	try {
            serverSocket = new ServerSocket(satelliteInfo.getPort());
	} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}        
        
        // start taking job requests in a server loop
        // ---------------------------------------------------------------
        // ...
    	while(true) {
            try{
                (new SatelliteThread(serverSocket.accept(), this)).start();
            }catch (IOException ex){
                System.err.println("[Server.Server] Warning: Error accepting client");
            } 
            
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
            System.out.println("Hello everyone");
        }
        
        @Override
        public void run() {
            // setting up object streams
            // ...
        	ObjectInputStream readFromNet = null;
                ObjectOutputStream writeToNet = null;
                Message message = null;
			try {
                            readFromNet = new ObjectInputStream(jobRequest.getInputStream());
                            writeToNet = new ObjectOutputStream(jobRequest.getOutputStream());
                            message = (Message) readFromNet.readObject();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
            
            // reading message
            // ...
            //System.out.println((String) message.getContent());
            
            switch (message.getType()) {
                case JOB_REQUEST:
                    // processing job request
                    // ...
			try {
                		Socket socket = new Socket("127.0.0.1", serverInfo.getPort());
                	} catch (UnknownHostException e1) {
                		// TODO Auto-generated catch block
                		e1.printStackTrace();
                	} catch (IOException e1) {
                		// TODO Auto-generated catch block
                		e1.printStackTrace();
                	}
                    Job job = (Job) message.getContent();
                    String classString = job.getToolName();
                    Object arguments = job.getParameters();

                    //get tool object
                    Tool tool = null;
                    try {
                        tool = satellite.getToolObject(classString);
                    } catch (UnknownToolException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException ex) {
                        System.err.println("[SatelliteThread.run] Error occured when retrieving class");
                        try {
                            readFromNet.close();
                            writeToNet.close();
                            System.err.println("... closing streams and returning");
                            return;
                            } catch (IOException ex1) {
                                System.err.println("[SatelliteThread.run] Error closing object streams");
                            }
                      }

                    Object result = tool.go(arguments);
                    try {
                        writeToNet.writeObject(result);
                    } catch (IOException ex) {
                        System.err.println("[SatelliteThread.run] Error when writing object to output stream");
                    }

			try {
                    	jobRequest.close();
                    } catch (IOException e) {
                    	// TODO Auto-generated catch block
                    	e.printStackTrace();
                    }
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
    public Tool getToolObject(String toolClassString) throws UnknownToolException, SecurityException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException{

        Tool toolObject = null;
        Class<?> toolClass = null;
        
        if ((toolObject = (Tool) toolsCache.get(toolClassString)) == null){
            try{
                toolClass = classLoader.loadClass(toolClassString);
            } catch (ClassNotFoundException ex){
                Logger.getLogger(Satellite.class.getName()).log(Level.SEVERE, null, ex);
                throw new UnknownToolException();
            }
            
            try{
                toolObject = (Tool) toolClass.getDeclaredConstructor().newInstance();
            }catch (InstantiationException ex){
                Logger.getLogger(Satellite.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IllegalAccessException ex) {
                Logger.getLogger(Satellite.class.getName()).log(Level.SEVERE, null, ex);          
            }catch (NoSuchMethodException ex) {
                Logger.getLogger(Satellite.class.getName()).log(Level.SEVERE, null, ex);
            }catch (SecurityException ex) {
                Logger.getLogger(Satellite.class.getName()).log(Level.SEVERE, null, ex);
            }catch (InvocationTargetException ex) {
                Logger.getLogger(Satellite.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("[DynCalculator] getOperation() - InvocationTargetException");                
            }
            
            // put the tool into cache
            toolsCache.put(toolClassString, toolObject);
        } else {
            System.out.println("[Satellite.getToolObject] Tool: \"" + toolClassString + "\" already in Cache");
        }
        
        return toolObject;
    }

    public static void main(String[] args) {
        // start the satellite
    	Satellite satellite = null;
    	if(args.length > 0) {
    		satellite = new Satellite(args[0], args[1], args[2]);
    	} 
        satellite.run();
    }
}
