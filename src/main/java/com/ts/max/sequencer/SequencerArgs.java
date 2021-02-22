package com.ts.max.sequencer;

import com.cycling74.max.Atom;
import lombok.Getter;

@Getter
class SequencerArgs {
    private final int numberOfVoices;
    private final int numberOfBeats;
    private final String sendName;
    private final String recvName;

    SequencerArgs(Atom[] maxArgs) {
        if (maxArgs.length != 4) {
            throw new IllegalArgumentException("Expected 4 arguments");
        }
        try {
            numberOfVoices = maxArgs[0].getInt();
            numberOfBeats = maxArgs[1].getInt();
            sendName = maxArgs[2].getString();
            recvName = maxArgs[3].getString();
        } catch (Throwable e) {
            throw new IllegalArgumentException(
                    "Expected: numberOfVoices(int) numberOfBeats(int) sendName(String) recvName(String)");
        }
    }

    Atom[] toMaxArgs() {
        return new Atom[] {
                Atom.newAtom(getNumberOfVoices()),
                Atom.newAtom(getNumberOfBeats()),
                Atom.newAtom(getSendName()),
                Atom.newAtom(getRecvName())
        };
    }
}
