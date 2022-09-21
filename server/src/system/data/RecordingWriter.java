/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package system.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedHashMap;

import system.time.Time;

public class RecordingWriter {
    private Recording recording;
    private int numSignals;
    private DateTimeFormatter dateTimeFormatter;
    
    public RecordingWriter(Recording recording) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.recording = recording;
    }
    
    public void writeSignalRow(LinkedHashMap<String, LinkedHashMap<String, String>> signalRow) {
        String dir = System.getProperty("user.dir");
        File file = new File(dir + "/recordings/" + this.recording.getName() + ".csv");
        
        // Create a new file if it doesn't exist yet or the number of signals
        // has changed:
        if (!file.exists() || this.numSignals != signalRow.size()) {
            this.writeHeader(file, signalRow);
            this.numSignals = signalRow.size();
        } else {
            appendRowToFile(file, signalRow);
        }
    }
    
    public void writeHeader(File file, LinkedHashMap<String, LinkedHashMap<String, String>> signalRow) {
        String header = "";
        Iterator<String> keyIterator = signalRow.keySet().iterator();
        header = header + "TIMESTAMP,SECONDS";
        
        if (signalRow.size() == 0) {
            header = header + "\n";
        } else {
            header = header + ",";
            while (keyIterator.hasNext()) {
                String key = (String) keyIterator.next();
                LinkedHashMap<String, String> signal = signalRow.get(key); // Get signal
                header = header + signal.get("name") + (keyIterator.hasNext() ? "," : "\n");
            }
        }
        appendLineToFile(file, header);
    }
    
    public void appendLineToFile(File file, String line) {
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(line);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void appendRowToFile(File file, LinkedHashMap<String, LinkedHashMap<String, String>> signalRow) {
        Iterator<String> keyIterator;
        keyIterator         = signalRow.keySet().iterator();
        String line         = "";
        long seconds        = Time.secondsUntilNow(Time.transportStartTime);

        line = line + LocalDateTime.now().format(this.dateTimeFormatter) + ",";
        line = line + seconds;
        
        if (signalRow.size() == 0) {
            line = line + "\n";
        } else {
            line = line + ",";
            while (keyIterator.hasNext()) {
                String key = (String) keyIterator.next();
                LinkedHashMap<String, String> signal = signalRow.get(key); // Get signal
                String value = (signal == null) ? "" : signal.get("value"); // Check if the signal was present at this time
                line = line + value + (keyIterator.hasNext() ? "," : "\n");
            }
        }

        appendLineToFile(file, line);
    }
}
