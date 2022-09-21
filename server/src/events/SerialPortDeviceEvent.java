/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package events;

import java.util.LinkedList;

public class SerialPortDeviceEvent {
    private LinkedList<String> messageQueue;

    public SerialPortDeviceEvent(LinkedList<String> messageQueue) {
        this.messageQueue = messageQueue;
    }
    
    public LinkedList<String> getMessageQueue() {
        return messageQueue;
    }

    public void setMessageQueue(LinkedList<String> messageQueue) {
        this.messageQueue = messageQueue;
    }
}
