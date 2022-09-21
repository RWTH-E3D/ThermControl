/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package listeners;

import events.DataEvent;

public interface DataListener {
    public void currentSignalsUpdated(DataEvent dataEvent);
}