package utils;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxClock;
import com.cycling74.max.MaxObject;

/**
 * A max object that is not designed to be left in the patcher as a MaxBox instance,
 * but will instead remove itself, leaving behind a different collection of MaxBox instances.
 */
public abstract class MaxBoxBuilder extends MaxObject {
    private final MaxClock cl;
    private MaxBox[] boxes;

    public MaxBoxBuilder(Atom[] args) {
        boxes = build(args);
        cl = new MaxClock(this, "cleanup");
        cl.delay(1.);
    }

    public abstract MaxBox[] build(Atom[] args);
    public abstract int[] getRect(int x, int y);

    public void cleanup() {
        int[] rect = this.getMaxBox().getRect();
        int x = rect[0];
        int y = rect[1];
        for (MaxBox box : boxes) {
            int[] updatedRect = getRect(x, y);
            box.setRect(
                    updatedRect[0],
                    updatedRect[1],
                    updatedRect[2],
                    updatedRect[3]);
            y = updatedRect[3];
        }
        this.getMaxBox().remove();
    }

    public void notifyDeleted() {
        cl.release();
    }
}
