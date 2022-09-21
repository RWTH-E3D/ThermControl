/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package events;

import system.data.DataContainer;

public class DataEvent {
    private String origin;
    private int numberOfSignals;
    private DataContainer dataContainer;
    
    public DataEvent(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    public DataContainer getDataContainer() {
        return dataContainer;
    }

    public void setDataContainer(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getNumberOfSignals() {
        return numberOfSignals;
    }

    public void setNumberOfSignals(int numberOfSignals) {
        this.numberOfSignals = numberOfSignals;
    }
}