/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package system.time;

import java.time.LocalDateTime;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import listeners.TimeThreadListener;

public class TimeThread {
    private static ArrayList<TimeThreadListenerContainer> listenerContainers;
    private static Thread thread;
    
    public static void init() {
        TimeThread.listenerContainers = new ArrayList<TimeThreadListenerContainer>();
        TimeThread.initThread();
    }
    
    public static void addListener(TimeThreadListener listener, long interval) {
        synchronized(TimeThread.listenerContainers) {
            TimeThread.listenerContainers.add(new TimeThreadListenerContainer(listener, interval));
        }
    }
    
    public static void removeListener(TimeThreadListener listener) {
        synchronized(TimeThread.listenerContainers) {
            for (TimeThreadListenerContainer listenerContainer : TimeThread.listenerContainers) {
                if (listenerContainer.getListener().equals(listener)) {
                    TimeThread.listenerContainers.remove(listenerContainer);
                    return;
                }
            }
        }
    }
    
    private static void initThread() {
        TimeThread.thread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    TimeThread.update();
                    try {
                        Thread.sleep(50); // TimeThread runs in 50 ms intervals
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        TimeThread.thread.start();
    }
    
    private static void update() {
        long interval = 0;
        long between = 0;
        LocalDateTime lastUpdated = null;
        TimeThreadListener listener = null;
        synchronized(TimeThread.listenerContainers) {
            for (TimeThreadListenerContainer listenerContainer : TimeThread.listenerContainers) {
                listener    = listenerContainer.getListener();
                interval    = listenerContainer.getInterval();
                lastUpdated = listenerContainer.getLastUpdate();
                
                if (lastUpdated == null) { // No update yet, update
                    listener.timeThreadTimedUpdate();
                    listenerContainer.setLastUpdate(LocalDateTime.now());
                } else {
                    between = ChronoUnit.MILLIS.between(lastUpdated, LocalDateTime.now());
                    if (between >= interval) {
                        listenerContainer.setLastUpdate(LocalDateTime.now());
                        listener.timeThreadTimedUpdate();
                    }
                }
            }
        }

        synchronized (TimeThread.listenerContainers) {
            for (TimeThreadListenerContainer listenerContainer : TimeThread.listenerContainers) {
                listener = listenerContainer.getListener(); 
                listener.timeThreadUpdate(); // Default thread time update
            }
        }
    }
}