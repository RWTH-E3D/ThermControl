/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package listeners;

import events.TransportEvent;

public interface TransportListener {
    public void transportEventReceived(TransportEvent evt);
}
