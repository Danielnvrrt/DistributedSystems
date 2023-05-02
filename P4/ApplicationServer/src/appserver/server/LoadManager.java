package appserver.server;

import java.util.ArrayList;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class LoadManager {

    static ArrayList satellites = null;
    static int lastSatelliteIndex = -1;

    public LoadManager() {
        satellites = new ArrayList<String>();
    }

    public void satelliteAdded(String satelliteName) {
        // add satellite
        // ...
        if (!satellites.contains(satelliteName)){
            satellites.add(satelliteName);
        }
        else {
            System.out.println("[Server.LoadManager]: " + satelliteName + " already in the list");
        }
    }


    public String nextSatellite() throws Exception {
        
        int numberSatellites = satellites.size();
        String name = null;
        synchronized (satellites) {
            // implement policy that returns the satellite name according to a round robin methodology
            // ...
            lastSatelliteIndex = (lastSatelliteIndex + 1) % numberSatellites;
            name = satellites.get(lastSatelliteIndex).toString();
        }

        return name;// ... name of satellite who is supposed to take job        
    }
}
