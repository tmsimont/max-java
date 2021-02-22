package com.ts.max.sequencer;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxPatcher;
import com.ts.max.utils.MaxPatcherUtils;

import java.util.ArrayList;
import java.util.List;

class VoiceOutletOutputs {
    private final List<MaxBox> outputs = new ArrayList<>();

    VoiceOutletOutputs(final MaxPatcher patcher, final SequencerArgs args) {
        for (int i = 0; i < args.getNumberOfVoices(); i++) {
            MaxBox output = MaxPatcherUtils.newDefault(
                    patcher,
                    30 + 2 * i,
                    30,
                    "outlet",
                    Atom.newAtom(args.getSendName() + i));
            output.hide();
            output.send("comment", new Atom[]{
                    Atom.newAtom("Voice output (sends via "+args.getSendName()+i+").")
            });
            outputs.add(output);
        }
    }

    MaxBox get(int i) {
        return outputs.get(i);
    }
}
