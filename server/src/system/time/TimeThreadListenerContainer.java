/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package system.time;
import java.time.LocalDateTime;

import listeners.TimeThreadListener;

public class TimeThreadListenerContainer {
    private TimeThreadListener listener;
    private long interval;
    private LocalDateTime lastUpdate;

    public TimeThreadListenerContainer(TimeThreadListener listener, long interval) {
        this.listener = listener;
        this.interval = interval;
    }
    
    public TimeThreadListener getListener() {
        return listener;
    }
    
    public void setListener(TimeThreadListener listener) {
        this.listener = listener;
    }
    
    public long getInterval() {
        return interval;
    }
    
    public void setInterval(long interval) {
        this.interval = interval;
    }
    
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
    
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}