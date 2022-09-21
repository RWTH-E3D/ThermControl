/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package events;

public class RangeSliderEvent {
    private float lowValue;
    private float highValue;
    
    public RangeSliderEvent(float lowValue, float highValue) {
        this.lowValue = lowValue;
        this.highValue = highValue;
    }
    
    public float getLowValue() {
        return lowValue;
    }
    public void setLowValue(float lowValue) {
        this.lowValue = lowValue;
    }
    public float getHighValue() {
        return highValue;
    }
    public void setHighValue(float highValue) {
        this.highValue = highValue;
    }
}