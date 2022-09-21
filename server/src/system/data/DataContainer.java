/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package system.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DataContainer {
    private LinkedHashMap<String, LinkedHashMap<String, String>> currentSignals; // Currently communicated signals
    private ArrayList<Recording> recordings; // All available recordings
    
    public DataContainer() {
        this.currentSignals = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        this.recordings = new ArrayList<Recording>();
    }
    
    public LinkedHashMap<String, LinkedHashMap<String, String>> getCurrentSignals() {
        return currentSignals;
    }
    
    public void setCurrentSignals(LinkedHashMap<String, LinkedHashMap<String, String>> currentSignals) {
        this.currentSignals = currentSignals;
    }

    public ArrayList<Recording> getRecordings() {
        return recordings;
    }

    public void setRecordings(ArrayList<Recording> recordings) {
        this.recordings = recordings;
    }
}