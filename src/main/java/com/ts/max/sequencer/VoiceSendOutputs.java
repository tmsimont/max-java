package com.ts.max.sequencer;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxPatcher;
import com.ts.max.utils.MaxPatcherUtils;

import java.util.ArrayList;
import java.util.List;

class VoiceSendOutputs {
    private final List<MaxBox> outputs = new ArrayList<>();

    VoiceSendOutputs(final MaxPatcher patcher, final SequencerArgs args) {
        for (int i = 0; i < args.getNumberOfVoices(); i++) {
            outputs.add(MaxPatcherUtils.newDefault(
                    patcher,
                    30 + 2 * i,
                    30,
                    "s",
                    Atom.newAtom(args.getSendName() + i)));
        }
    }

    MaxBox get(int i) {
        return outputs.get(i);
    }
}
