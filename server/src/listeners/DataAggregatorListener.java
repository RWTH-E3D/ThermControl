/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package listeners;

import events.DataAggregatorStatusChangedEvent;

public interface DataAggregatorListener {
    public void statusChanged(DataAggregatorStatusChangedEvent e);
}
