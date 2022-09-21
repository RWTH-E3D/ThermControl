/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package system.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class HashMapTools {
    public static ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>> copyHashMapList(ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>> original) {
        ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>> copy = new ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>>();
        
        for (LinkedHashMap<String, LinkedHashMap<String, String>> nestedHashMap : original) {
            copy.add(HashMapTools.copyNestedHashMap(nestedHashMap));
        }
        
        return copy;
    }
    
    public static LinkedHashMap<String, LinkedHashMap<String, String>> copyNestedHashMap(LinkedHashMap<String, LinkedHashMap<String, String>> original) {
        LinkedHashMap<String, LinkedHashMap<String, String>> copy = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        
        for (Entry<String, LinkedHashMap<String, String>> entry : original.entrySet()) { // Iterate through signals
            copy.put(entry.getKey(), HashMapTools.copyHashMap(entry.getValue()));
        }
        
        return copy;
    }
    
    public static LinkedHashMap<String, String> copyHashMap(LinkedHashMap<String, String> original) {
        LinkedHashMap<String, String> copy = new LinkedHashMap<String, String>();
        return new LinkedHashMap<String, String>(original);
    }
}
