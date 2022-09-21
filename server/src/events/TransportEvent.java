/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package events;

public class TransportEvent {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public static final int REWIND  = 0;
    public static final int FORWARD = 1;
    public static final int PLAY    = 2;
    public static final int PAUSE   = 3;
    public static final int STOP    = 4;
    
    private int type;
    
    public TransportEvent(int type) {
        this.setType(type);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}