package com.ts.max.utils;

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
        cl.delay(500.);
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
        // this causes max to crash?
        // this.getMaxBox().remove();
        // this.getMaxBox().send("yo wat up", new Atom[]{});
        killer = MaxPatcherUtils.newDefault(
                this.getParentPatcher(),
                0,
                0,
                "message",
                Atom.newAtom("killbuilder")
        );
        this.getParentPatcher().connect(killer, 0, this.getMaxBox(), 0);
    }

    private MaxBox killer;

    public void anything(String message, Atom[] args) {
        if (message.equals("killbuilder")) {
            this.getMaxBox().remove();
        }
    }

    public void notifyDeleted() {
        cl.release();
    }
}
