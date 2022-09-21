/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package system;

import java.util.ArrayList;
import java.util.LinkedHashMap;
/**
 * @author Henning Metzmacher, E3D, RWTH Aaachen University
 */
public class Message {
    private ArrayList<LinkedHashMap<String, String>> signals;
    
    public Message() {
        this.setSignals(new ArrayList<LinkedHashMap<String, String>>());
    }

    public ArrayList<LinkedHashMap<String, String>> getSignals() {
        return signals;
    }

    public void setSignals(ArrayList<LinkedHashMap<String, String>> signals) {
        this.signals = signals;
    }
    
    public void addSignal(LinkedHashMap<String, String> signal) {
        this.signals.add(signal);
    }
}
