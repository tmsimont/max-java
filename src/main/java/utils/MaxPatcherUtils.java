package utils;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxPatcher;

/**
 * Provides some utils for a cleaner MaxPatcher API
 */
public class MaxPatcherUtils {

    public static MaxBox newDefault(
            MaxPatcher patcher,
            int x,
            int y,
            String type,
            Atom... args) {
        return patcher.newDefault(
                x,
                y,
                type,
                args);
    }

    public static MaxBox newDefault(
            MaxPatcher patcher,
            String type,
            Atom... args) {
        return patcher.newDefault(
                0,
                0,
                type,
                args);
    }

    public static MaxBox newHidden(
            MaxPatcher patcher,
            String type,
            Atom... args) {
        MaxBox box = patcher.newDefault(
                0,
                0,
                type,
                args);
        box.hide();
        return box;
    }

    public static MaxBox newPresentable(
            MaxPatcher patcher,
            String type,
            int x,
            int y,
            int height,
            int width) {
        MaxBox box = patcher.newDefault(
                x,
                y,
                type,
                getPresentationArgs(x, y, height, width));
        box.hide();
        return box;
    }

    public static Atom[] getPresentationArgs(int x, int y, int height, int width) {
        return new Atom[]{
                Atom.newAtom("@presentation"),
                Atom.newAtom(1),
                Atom.newAtom("@presentation_rect"),
                Atom.newAtom(x),
                Atom.newAtom(y),
                Atom.newAtom(height),
                Atom.newAtom(width)};
    }
}
