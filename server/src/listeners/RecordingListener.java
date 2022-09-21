/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package listeners;

import events.RecordingEvent;

public interface RecordingListener {
    public void recordingAdded(RecordingEvent recordingEvent);
    public void recordingRemoved(RecordingEvent recordingEvent);
}
