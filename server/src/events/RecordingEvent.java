/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package events;

import system.data.Recording;

public class RecordingEvent {
    private Recording recording;
    
    public RecordingEvent(Recording recording) {
        this.recording = recording;
    }

    public Recording getRecording() {
        return recording;
    }

    public void setRecording(Recording recording) {
        this.recording = recording;
    }
}