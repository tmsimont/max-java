package com.ts.max.sequencer;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxPatcher;
import com.ts.max.utils.MaxPatcherUtils;

class CountDriver {
    private final MaxBox counter;
    private final MaxBox inBang;
    private final MaxBox inRecv;
    private final MaxBox rewindMessage;
    private final MaxBox inRewind;
    private final MaxBox inRecvR;

    CountDriver(final MaxPatcher patcher,
                final SequencerArgs args) {
        // Primary max element to keep track of count.
        counter = MaxPatcherUtils.newHidden(
                patcher,
                "counter",
                Atom.newAtom(1),
                Atom.newAtom(args.getNumberOfBeats()));

        // Inlet for receiving bangs via patchcord.
        inBang = MaxPatcherUtils.newHidden(patcher, "inlet");
        inBang.send("comment", new Atom[]{
                Atom.newAtom("Bang ("+args.getRecvName()+") to advance sequence 1 step.")
        });
        patcher.connect(inBang, 0, counter, 0);

        // Receive input for receiving bangs via send.
        inRecv = MaxPatcherUtils.newHidden(
                patcher,
                "r",
                Atom.newAtom(args.getRecvName()));
        patcher.connect(inRecv, 0, counter, 0);

        // Message we can send to our counter to rewind it.
        // Build a message object.
        rewindMessage = MaxPatcherUtils.newHidden(patcher, "message");
        // Set the message to "0"
        rewindMessage.send("set", new Atom[]{ Atom.newAtom(0)});
        // Connect the "0" message to our counter at inlet 3, which is the "set immediately to this value" inlet.
        // This will set the counter to "0" when we bang on rewindMessage.
        patcher.connect(rewindMessage, 0, counter, 3);

        // Inlet to receive bangs and hit our rewind message
        inRewind = patcher.newDefault(20, 0, "inlet", new Atom[]{});
        inRewind.hide();
        inRewind.send("comment", new Atom[]{
                Atom.newAtom("Bang ("+args.getRecvName()+"-R) to reset to beginning of sequence.")
        });
        patcher.connect(inRewind, 0, rewindMessage, 0);

        // Receiver input for receiving rewind via send.
        inRecvR = MaxPatcherUtils.newHidden(
                patcher,
                "r",
                Atom.newAtom(args.getRecvName() + "-R"));
        patcher.connect(inRecvR, 0, rewindMessage, 0);
    }

    MaxBox getSignalDriver() {
        return counter;
    }
}
