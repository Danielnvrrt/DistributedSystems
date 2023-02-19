package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import utils.NetworkUtilities;

public class CreatePropertiesFile {
   public static void main(String args[]) throws IOException {
      //Instantiating the properties file
      Properties props = new Properties();
      //Populating the properties file
      props.put("SERVER_PORT", "9999");
      props.put("SERVER_IP", NetworkUtilities.getMyIP());
      //Instantiating the FileInputStream for output file
      String path = "/home/usuario/Escritorio/DS_Aux/DistributedSystems/CentralServer/config/ServerNodeDefaults.properties";
      FileOutputStream outputStrem = new FileOutputStream(path);
	//Storing the properties file
      props.store(outputStrem, "This is a sample properties file");
      System.out.println("Properties file created......");
   }
}
