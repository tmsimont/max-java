package sequencer;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxPatcher;
import utils.MaxPatcherUtils;

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
                Atom.newAtom(args.beats));

        // Inlet for receiving bangs via patchcord.
        inBang = MaxPatcherUtils.newHidden(patcher, "inlet");
        inBang.send("comment", new Atom[]{
                Atom.newAtom("Bang to advance sequence 1 step.")
        });
        patcher.connect(inBang, 0, counter, 0);

        // Receive input for receiving bangs via send.
        inRecv = MaxPatcherUtils.newHidden(
                patcher,
                "r",
                Atom.newAtom(args.recvName));
        patcher.connect(inRecv, 0, counter, 0);

        // Message we can send to our counter to rewind it.
        rewindMessage = MaxPatcherUtils.newHidden(patcher, "message");
        rewindMessage.send("set", new Atom[]{ Atom.newAtom(1)});
        patcher.connect(rewindMessage, 0, counter, 0);

        // Inlet to receive bangs and hit our rewind message
        inRewind = patcher.newDefault(20, 0, "inlet", new Atom[]{});
        inRewind.hide();
        inRewind.send("comment", new Atom[]{
                Atom.newAtom("Bang to reset to beginning of sequence.")
        });
        patcher.connect(inRewind, 0, rewindMessage, 0);

        // Receiver input for receiving rewind via send.
        inRecvR = MaxPatcherUtils.newHidden(
                patcher,
                "r",
                Atom.newAtom(args.recvName + "-R"));
        patcher.connect(inRecvR, 0, rewindMessage, 0);
    }

    MaxBox getSignalDriver() {
        return counter;
    }
}
