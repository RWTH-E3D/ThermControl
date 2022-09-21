/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package system.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.SwingUtilities;

import events.DataAggregatorStatusChangedEvent;
import events.DataEvent;
import events.RecordingEvent;
import listeners.DataAggregatorListener;
import listeners.DataListener;
import listeners.RecordingListener;
import system.time.Time;
import system.time.TimeThread;

public class DataAggregator {
    public static final int STATUS_STOPPED      = 0;
    public static final int STATUS_PLAYING      = 1;
    public static final int STATUS_RECORDING    = 2;
    
    private static ExecutorService executor;
    
    public static DataContainer dataContainer;
    public static ArrayList<DataListener> dataListeners;
    public static ArrayList<RecordingListener> recordingListeners;
    public static ArrayList<DataAggregatorListener> dataAggregatorListeners;
    private static int status;
    
    public static void init() {
        DataAggregator.dataListeners = new ArrayList<DataListener>();
        DataAggregator.recordingListeners = new ArrayList<RecordingListener>();
        DataAggregator.dataAggregatorListeners = new ArrayList<DataAggregatorListener>();
        DataAggregator.dataContainer = new DataContainer();
        
        executor = Executors.newFixedThreadPool(5);
    }
    
    public static void createDataEvent(final ArrayList<LinkedHashMap<String, String>> signals, final String origin) {
        DataAggregator.executor.execute(new Runnable() {
            public void run() {
                // Update data container current signals:
                synchronized (DataAggregator.dataContainer.getCurrentSignals()) {
                    for (LinkedHashMap<String, String> signal : signals) {
                        DataAggregator.dataContainer.getCurrentSignals().put(signal.get("name"), signal);
                    }
                }
                
                // Notify listeners:
                synchronized(DataAggregator.dataListeners) {
                    for (DataListener dataListener : DataAggregator.dataListeners) {
                        DataEvent dataEvent = new DataEvent(DataAggregator.dataContainer);
                        dataEvent.setOrigin(origin);
                        dataEvent.setNumberOfSignals(signals.size());
                        dataListener.currentSignalsUpdated(dataEvent);
                    }
                }
            }
        });
    }
    
    public static void addDataListener(DataListener dataListener) {
        synchronized(DataAggregator.dataListeners) {
            DataAggregator.dataListeners.add(dataListener);
        }
    }
    
    public static void removeDataListener(DataListener dataListener) {
        synchronized(DataAggregator.dataListeners) {
            DataAggregator.dataListeners.remove(dataListener);
        }
    }
    
    public static void addRecordingListener(RecordingListener recordingListener) {
        synchronized (DataAggregator.recordingListeners) {
            DataAggregator.recordingListeners.add(recordingListener);
        }
    }
    
    public static void removeRecordingListner(RecordingListener recordingListener) {
        synchronized (DataAggregator.recordingListeners) {
            DataAggregator.recordingListeners.remove(recordingListener);
        }
    }
    
    public static void addDataAggregatorListener(DataAggregatorListener dataAggregatorListener) {
        synchronized (DataAggregator.dataAggregatorListeners) {
            DataAggregator.dataAggregatorListeners.add(dataAggregatorListener);
        }
    }
    
    public static void removeDataAggregatorListener(DataAggregatorListener dataAggregatorListener) {
        synchronized (DataAggregator.dataAggregatorListeners) {
            DataAggregator.dataAggregatorListeners.remove(dataAggregatorListener);
        }
    }
    
    public static void addRecording(Recording recording) {
        synchronized(DataAggregator.recordingListeners) {
            for (RecordingListener recordingListener : DataAggregator.recordingListeners) {
                recordingListener.recordingAdded(new RecordingEvent(recording));
            }
        }
    }
    
    public static void removeRecording(Recording recording) {
        synchronized (DataAggregator.dataContainer.getRecordings()) {
            DataAggregator.dataContainer.getRecordings().remove(recording);
        }
        
        DataAggregator.removeDataAggregatorListener(recording);
        TimeThread.removeListener(recording);
        
        // Notify recording listeners:
        synchronized (DataAggregator.recordingListeners) {
            for (RecordingListener recordingListener : DataAggregator.recordingListeners) {
                recordingListener.recordingRemoved(new RecordingEvent(recording));
            }
        }
    }
    
    public static void startPlaying() {
        DataAggregator.status = STATUS_PLAYING;
        Time.transportStartTime = LocalDateTime.now();
        DataAggregator.notifyDataAggregatorListeners();
    }
    
    public static boolean isPlaying() {
        return DataAggregator.status == STATUS_PLAYING;
    }
    
    public static void startRecording() {
        DataAggregator.status = STATUS_RECORDING;
        Time.transportStartTime = LocalDateTime.now();
        DataAggregator.notifyDataAggregatorListeners();
    }
    
    public static boolean isRecording() {
        return DataAggregator.status == STATUS_RECORDING;
    }
    
    public static void stop() {
        DataAggregator.status = STATUS_STOPPED;
        DataAggregator.notifyDataAggregatorListeners();
    }
    
    private static void notifyDataAggregatorListeners() {
        synchronized (DataAggregator.dataAggregatorListeners) {
            for (DataAggregatorListener listener : DataAggregator.dataAggregatorListeners) {
                listener.statusChanged(new DataAggregatorStatusChangedEvent(DataAggregator.status));
            }
        }
    }
}
