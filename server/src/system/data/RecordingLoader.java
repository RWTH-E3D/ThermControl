/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package system.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class RecordingLoader {
    public Recording loadRecording(File file) {
        Recording recording = new Recording();
        
        try {
            Reader in = new FileReader(file);
            CSVParser parser = new CSVParser(in, CSVFormat.EXCEL.withHeader());
            Map<String, Integer> headerMap = parser.getHeaderMap();
            
            for (CSVRecord record : parser) { // Iterate through CSV records
                long seconds = Long.parseLong(record.get("SECONDS"));
                LinkedHashMap<String, LinkedHashMap<String, String>> signals = new LinkedHashMap<String, LinkedHashMap<String, String>>();
                
                for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
                    int i = (int) entry.getValue();
                    String columnHeader = entry.getKey();
                    LinkedHashMap<String, String> systemSignal = SystemDb.systemSignals.get(columnHeader);
                    
                    if (systemSignal == null) {
                        continue;
                    } else {
                        LinkedHashMap<String, String> signal = HashMapTools.copyHashMap(systemSignal);
                        signal.put("value", record.get(i)); // Get the value of the signal in the CSV row
                        signals.put(columnHeader, signal);
                    }
                }
                
                recording.getEntries().put(seconds, signals);
            }
            
            parser.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        
        return recording;
    }
}
