/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package system.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.common.io.Files;

import listeners.SystemDbListener;

public class SystemDb {
    public static LinkedHashMap<String, LinkedHashMap<String, String>> systemSignals;
    public static LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>> systemSignalTypes;
    public static LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>> systemSignalSerialGroups;
    public static ArrayList<String> systemSignalHeaders;
    private static String systemSignalsPath = SystemDb.class.getResource("../../SignalDb.csv").getPath();
    
    public static void init() {
        loadSystemSignals();
    }
    
    public static void writeSystemSignals(String[][] data, String[] headers) {
        try {
            // Create a backup file before updating the system signals:
            File original = new File(systemSignalsPath);
            File copy = new File(systemSignalsPath + "-BAK");
            Files.copy(original, copy);
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(systemSignalsPath));
            PrintWriter printWriter = new PrintWriter(writer);
            
            
            // Write header:
            String header = "";
            for (int i = 0; i < headers.length; i++) {
                header = header + headers[i];
                header = header + ((i < headers.length - 1) ? "," : "");
            }
            printWriter.println(header);
            
            // Write data:
            String line = "";
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    line = line + data[i][j] + ((j < data[0].length - 1) ? "," : "");
                }
                printWriter.println(line);
                line = "";
            }
            
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static LinkedHashMap<String, String> augmentSignal(LinkedHashMap<String, String> signal) {
        LinkedHashMap<String, String> systemSignal = SystemDb.systemSignals.get(signal.get("name"));
        
        if (systemSignal == null) {
            return null;
        }
        
        for (Map.Entry<String, String> entry : systemSignal.entrySet()) {
            if (!signal.containsKey(entry.getKey())) {
                signal.put(entry.getKey(), entry.getValue());
            }
        }
        return signal;
    }
    
    public static void loadSystemSignals() {
        // Create a new system signals linked hash map:
        SystemDb.systemSignals = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        SystemDb.systemSignalTypes = new LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>>();
        SystemDb.systemSignalSerialGroups = new LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>>();
        SystemDb.systemSignalHeaders = new ArrayList<String>();
        
        try {
            Reader in = new FileReader(systemSignalsPath);
            CSVParser parser = new CSVParser(in, CSVFormat.EXCEL.withHeader());
            Map<String, Integer> headerMap = parser.getHeaderMap();
            
            // Create list of signal headers:
            for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
                String columnHeader = entry.getKey();
                SystemDb.systemSignalHeaders.add(columnHeader);
            }
            
            for (CSVRecord record : parser) { // Iterate through CSV records
                LinkedHashMap<String, String> systemSignal = new LinkedHashMap<String, String>();
                for (Map.Entry<String, Integer> entry : headerMap.entrySet()) { // Iterate through headers
                    String key = entry.getKey();
                    systemSignal.put(key, record.get(key));
                }
                
                SystemDb.systemSignals.put(record.get("name"), systemSignal); // Hash map grouped by signal name
                
                // Add to hash map ordered by system type:
                ArrayList<LinkedHashMap<String, String>> typeSignals = SystemDb.systemSignalTypes.get(systemSignal.get("type"));
                if (typeSignals == null) { // Array list hasn't been created yet
                    typeSignals = new ArrayList<LinkedHashMap<String, String>>();
                    SystemDb.systemSignalTypes.put(systemSignal.get("type"), typeSignals);
                }
                typeSignals.add(systemSignal); // Add system signal to type signal list
                
                // Add to hash map ordered by serial port group:
                if (!record.get("serial_port_group").equals("") && !record.get("serial_port_index").equals("")) { 
                    if (SystemDb.systemSignalSerialGroups.get(record.get("serial_port_group")) == null) {
                        SystemDb.systemSignalSerialGroups.put(record.get("serial_port_group"), new ArrayList<LinkedHashMap<String, String>>());
                    }
                    
                    int serialPortIndex = Integer.parseInt(record.get("serial_port_index"));
                    ArrayList<LinkedHashMap<String, String>> serialGroupSignals = SystemDb.systemSignalSerialGroups.get(record.get("serial_port_group"));
                    
                    // It is possible for the serial group signal array list to look like this:
                    // Signal name  Serial port index
                    // null         0
                    // null         1
                    // null         2
                    // T_NTC_01     3
                    // T_P204       4
                    // ...          ...
                    
                    if (serialGroupSignals.size() <= serialPortIndex) {
                        for (int i = serialGroupSignals.size(); i <= serialPortIndex; i++) {
                            serialGroupSignals.add(null); // Fill blanks with null objects
                        }
                    }
                    
                    serialGroupSignals.set(Integer.parseInt(record.get("serial_port_index")), systemSignal); // Add signal according to serial port index
                }
            }
            
            parser.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}