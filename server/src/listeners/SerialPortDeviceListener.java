/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package listeners;

import java.util.LinkedList;

import events.SerialPortDeviceEvent;

public interface SerialPortDeviceListener {
    public void serialPortDeviceEvent(SerialPortDeviceEvent e);
}
