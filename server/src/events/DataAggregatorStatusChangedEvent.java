/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package events;

public class DataAggregatorStatusChangedEvent {
    private int status;

    public DataAggregatorStatusChangedEvent(int status) {
        this.status = status;
    }
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
