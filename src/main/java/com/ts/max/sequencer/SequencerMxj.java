package com.ts.max.sequencer;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;
import lombok.AllArgsConstructor;
import com.ts.max.bpatcher.BPatcherSpecification;

public class SequencerMxj extends MaxObject {
    private Sequencer sequencer;

    public void build(Atom[] args) {
        try {
            MaxPatcher patcher = this.getParentPatcher();
            SequencerArgs sequencerArgs = new SequencerArgs(args);
            sequencer = new Sequencer(patcher, sequencerArgs);
        } catch (Throwable e) {
            error(e.getMessage());
        }
    }

    static int expectedWidth(SequencerArgs sequencerArgs) {
        return (int) Math.ceil(sequencerArgs.getNumberOfBeats() * VisualControls.HEIGHT_PER_BEAT);
    }

    static int expectedHeight(SequencerArgs sequencerArgs) {
        return (int) Math.ceil(sequencerArgs.getNumberOfVoices() * VisualControls.WIDTH_PER_VOICE);
    }

    public static BPatcherSpecification getBPatcherSpecification(Atom[] args) {
        return new Specification(new SequencerArgs(args));
    }

    @AllArgsConstructor
    static class Specification implements BPatcherSpecification {
        private final SequencerArgs sequencerArgs;

        /**
         * The name of the bpatcher file (without maxpat suffix)
         */
        public String getBPatcherName() {
            return "seqmxj";
        }

        /**
         * The name of a child element of the bpatcher that can understand an initialization
         * message.
         */
        public String getNameOfInitializerMaxObject() {
            return "seqmxj";
        }

        /**
         * The name of initializer method that the initializer max object will understand.
         */
        public String getNameOfInitializerMethod() {
            return "build";
        }

        /**
         * The named initializer object will understand a message with these args.
         * The first arg in the list is the message name.
         */
        public Atom[] getInitializerArgs() {
            return sequencerArgs.toMaxArgs();
        }

        public int getWidth() {
            return expectedWidth(sequencerArgs);
        }

        public int getHeight() {
            return expectedHeight(sequencerArgs);
        }
    }
}
