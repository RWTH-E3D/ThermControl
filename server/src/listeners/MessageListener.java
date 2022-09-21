/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package listeners;

import events.MessageEvent;

public interface MessageListener {
    public void messageReceived(MessageEvent evt);
}