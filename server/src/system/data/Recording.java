/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package system.data;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.SwingUtilities;

import config.Config;
import events.DataAggregatorStatusChangedEvent;
import events.DataEvent;
import events.RecordingEvent;
import listeners.DataAggregatorListener;
import listeners.DataListener;
import listeners.RecordingListener;
import listeners.TimeThreadListener;
import server.Broadcaster;
import system.time.Time;
import system.time.TimeThread;

public class Recording implements DataListener, TimeThreadListener, DataAggregatorListener {
    private TreeMap<Long, LinkedHashMap<String, LinkedHashMap<String, String>>> entries;
    private LinkedHashMap<String, LinkedHashMap<String, String>> currentSignals;
    private boolean broadcasting;
    private boolean recording;
    private String name;
    private long recordingInterval;
    private RecordingWriter recordingWriter;
    private Broadcaster broadcaster;
    
    public Recording() {
        this.recording = false;
        this.broadcasting = false;
        this.entries = new TreeMap<Long, LinkedHashMap<String, LinkedHashMap<String, String>>>();
        this.recordingInterval = Long.parseLong(Config.attribute("Measurement", "defaultRecordingInterval"));
        this.broadcaster = new Broadcaster();
        TimeThread.addListener(this, this.recordingInterval);
        this.recordingWriter = new RecordingWriter(this);
        this.setDefaultName();
        
        DataAggregator.addDataListener(this);
        DataAggregator.addDataAggregatorListener(this);
    }

    public TreeMap<Long, LinkedHashMap<String, LinkedHashMap<String, String>>> getEntries() {
        return entries;
    }

    public void currentSignalsUpdated(DataEvent dataEvent) {
        this.currentSignals = dataEvent.getDataContainer().getCurrentSignals();
    }

    public void timeThreadUpdate() {}

    public void timeThreadTimedUpdate() {
        final LinkedHashMap<String, LinkedHashMap<String, String>> copy;
        if (this.currentSignals != null) {         
            if (this.recording && DataAggregator.isRecording()) {
                synchronized(this.currentSignals) {
                    // Create a copy of the nested signals hash map:
                    copy = HashMapTools.copyNestedHashMap(this.currentSignals);
                }
                
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        // Copy current signals into the tree map:
                        synchronized(Recording.this.entries) {
                            Recording.this.entries.put(Time.millisUntilNow(Time.transportStartTime), copy);
                        }
                        Recording.this.recordingWriter.writeSignalRow(copy);
                    }
                });
            }
        }
        
        if (this.broadcasting && (DataAggregator.isPlaying() || DataAggregator.isRecording())) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    long millis = Time.millisUntilNow(Time.transportStartTime);
                    double seconds = millis / 1000.0; 
                    Long floor = Recording.this.entries.floorKey((long) seconds);
                    Long ceiling = Recording.this.entries.ceilingKey((long) seconds);
                    long key = 0;
                    
                    if (floor != null && (floor - seconds) < 5) {
                        key = floor;
                    } else if (ceiling != null && (ceiling - seconds) < 5) {
                        key = ceiling;
                    } else {
                        return;
                    }
                    
                    final LinkedHashMap<String, LinkedHashMap<String, String>> signalRow;
                    synchronized(Recording.this.entries) {
                        signalRow = Recording.this.entries.get(key);
                        
                    }
                    
                    if (signalRow != null) {
                        Thread thread = new Thread(new Runnable() {
                            public void run() {
                                Recording.this.broadcaster.broadcast(signalRow);
                            }
                        });
                        thread.run();
                    }
                }
            });
        }
    }

    public boolean isBroadcasting() {
        return broadcasting;
    }

    public void setBroadcasting(boolean broadcasting) {
        this.broadcasting = broadcasting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setDefaultName() {
        this.name = Time.timestamp(LocalDateTime.now()) + "-" + Config.attribute("Measurement", "defaultRecordingName");
    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    public long getRecordingInterval() {
        return recordingInterval;
    }

    public void setRecordingInterval(long recordingInterval) {
        this.recordingInterval = recordingInterval;
    }

    public void statusChanged(DataAggregatorStatusChangedEvent e) {
        if (DataAggregator.isRecording()) {
            // Update the default name:
            this.setDefaultName();
        }
    }
}