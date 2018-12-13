package com.ts.max.sequencer;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxPatcher;
import com.ts.max.utils.MaxPatcherUtils;
import com.ts.max.utils.PasteAndLoadBang;

import java.util.ArrayList;
import java.util.List;

class VisualControls {
    static final double HEIGHT_PER_BEAT = 16.125;
    static final double WIDTH_PER_VOICE = 16.666;

    private final MaxBox matrixControl;
    private final List<MaxBox> xLabels;
    private final List<MaxBox> yLabels;

    VisualControls(final MaxPatcher patcher,
                   final SequencerArgs args) {
        matrixControl = getMatrixControlBox(patcher, args);

        // Bang matrix on load and on copy/paste
        PasteAndLoadBang.forMaxBox(patcher, matrixControl);

        // Label the y axis
        yLabels = new ArrayList<>();
        for (int i = 0; i < args.getNumberOfVoices(); i++) {
            yLabels.add(MaxPatcherUtils.newPresentable(
                    patcher,
                    "comment",
                    (16 * args.getNumberOfBeats()),
                    (16 * i),
                    30,
                    16));
            yLabels.get(i).send("set", new Atom[]{Atom.newAtom(""+i)});
        }

        // Label the x axis
        xLabels = new ArrayList<>();
        for (int i = 0; i < args.getNumberOfBeats(); i++) {
            xLabels.add(MaxPatcherUtils.newPresentable(
                    patcher,
                    "comment",
                    (16 * i),
                    (16 * args.getNumberOfVoices()),
                    30,
                    16));
            xLabels.get(i).send("set", new Atom[]{Atom.newAtom(""+(i+1))});
        }

        // Label the send/recv names
        MaxBox label = MaxPatcherUtils.newPresentable(
                patcher,
                "comment",
                0,
                (16 * (args.getNumberOfVoices() + 1)),
                100,
                16);
        label.send("set", new Atom[]{Atom.newAtom("s: " + args.getSendName() + " r: " + args.getRecvName())});
    }

    private MaxBox getMatrixControlBox(
            MaxPatcher patcher,
            SequencerArgs args) {
        // Build a named matrixctrl max box.
        String MATRIX_NAME = "main-matrix";
        MaxBox box = MaxPatcherUtils.newPresentable(
                patcher,
                "matrixctrl",
                0,
                0,
                (int) (HEIGHT_PER_BEAT * args.getNumberOfBeats()),
                (int) (WIDTH_PER_VOICE * args.getNumberOfVoices()));
        box.send("rows", new Atom[] {Atom.newAtom(args.getNumberOfVoices())});
        box.send("columns", new Atom[] {Atom.newAtom(args.getNumberOfBeats())});
        box.send("scale", new Atom[] {Atom.newAtom(false)});
        box.send("autosize", new Atom[] {Atom.newAtom(true)});
        box.setName(MATRIX_NAME);

        // Connect the box to a savematrix.js instance
        MaxBox savematrix = MaxPatcherUtils.newHidden(
                patcher,
                "js",
                Atom.newAtom("savematrix.js"),
                Atom.newAtom(MATRIX_NAME));

        // Connect
        patcher.connect(box, 0, savematrix, 0);

        // Bang savematrix on copy/paste as well as load
        PasteAndLoadBang.forMaxBox(patcher, savematrix);
        return box;
    }

    MaxBox getRoutingControl() {
        return this.matrixControl;
    }

    public void remove() {
        matrixControl.remove();
        while(yLabels.size() > 0) {
            yLabels.remove(yLabels.size() - 1).remove();
        }
        while(xLabels.size() > 0) {
            xLabels.remove(xLabels.size() - 1).remove();
        }
    }
}
