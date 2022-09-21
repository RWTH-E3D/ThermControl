/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package events;

import system.Message;

public class MessageEvent {

    private Message message;

    public MessageEvent(Message message) {
        this.setMessage(message);
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
