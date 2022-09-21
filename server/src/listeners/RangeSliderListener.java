/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package listeners;

import events.RangeSliderEvent;

public interface RangeSliderListener {
    public void knobMoved(RangeSliderEvent e);
}