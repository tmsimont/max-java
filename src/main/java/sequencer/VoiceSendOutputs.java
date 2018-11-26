package sequencer;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxPatcher;
import utils.MaxPatcherUtils;

import java.util.ArrayList;
import java.util.List;

class VoiceSendOutputs {
    private final List<MaxBox> outputs = new ArrayList<>();

    VoiceSendOutputs(final MaxPatcher patcher, final SequencerArgs args) {
        for (int i = 0; i < args.voices; i++) {
            outputs.add(MaxPatcherUtils.newDefault(
                    patcher,
                    args.x + 30 + 2 * i,
                    args.y + 30,
                    "s",
                    Atom.newAtom(args.sendName + i)));
        }
    }

    MaxBox get(int i) {
        return outputs.get(i);
    }
}
