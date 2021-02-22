package com.ts.max.bsmp;

import com.cycling74.max.Atom;
import lombok.Getter;

@Getter
class BangSamplerArgs {
    private final int numberOfSamples;
    private final int offsetIndex;
    private final String sendName;

    BangSamplerArgs(Atom[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Expected 3 args");
        }
        try {
            numberOfSamples = args[0].getInt();
            offsetIndex = args[1].getInt();
            sendName = args[2].getString();
        } catch (Throwable e) {
            throw new IllegalArgumentException("Invalid arguments: " + e.getMessage());
        }
    }

    Atom[] toMaxArgs() {
        return new Atom[] {
                Atom.newAtom(getNumberOfSamples()),
                Atom.newAtom(getOffsetIndex()),
                Atom.newAtom(getSendName())
        };
    }
}
