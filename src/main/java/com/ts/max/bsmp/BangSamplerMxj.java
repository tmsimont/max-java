package com.ts.max.bsmp;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;
import com.ts.max.bpatcher.BPatcherSpecification;
import lombok.AllArgsConstructor;

public class BangSamplerMxj extends MaxObject {
    MaxBox[] maxBoxes;
    MaxBox[] inlets;
    MaxBox[] outlets;
    private static final int PER_SAMPLER_HEIGHT = 60;
    private static final int PER_SAMPLER_WIDTH = 350;

    public void build(Atom[] args) {
        MaxPatcher patcher = this.getParentPatcher();
        BangSamplerArgs bangSamplerArgs = new BangSamplerArgs(args);
        maxBoxes = new MaxBox[bangSamplerArgs.getNumberOfSamples()];
        inlets = new MaxBox[bangSamplerArgs.getNumberOfSamples()];
        outlets = new MaxBox[bangSamplerArgs.getNumberOfSamples()];
        for (int i = 0; i < bangSamplerArgs.getNumberOfSamples(); i++) {
            String name = bangSamplerArgs.getSendName() + (bangSamplerArgs.getOffsetIndex() + i);
            String signalName = "signal-" + name;
            String bufferName = "buffer-" + name;
            maxBoxes[i] = patcher.newDefault(
                    0,
                    i * PER_SAMPLER_HEIGHT,
                    "bpatcher",
                    new Atom[]{
                            Atom.newAtom("TxtBangSamp"),
                            Atom.newAtom("@args"),
                            Atom.newAtom(bufferName),
                            Atom.newAtom(signalName),
                            Atom.newAtom(name),
                            Atom.newAtom(bangSamplerArgs.getSendName()),
                            Atom.newAtom("@embed"),
                            Atom.newAtom(1),
                            Atom.newAtom("@presentation"),
                            Atom.newAtom(1),
                            Atom.newAtom("@presentation_rect"),
                            Atom.newAtom(0),
                            Atom.newAtom(i * PER_SAMPLER_HEIGHT),
                            Atom.newAtom(PER_SAMPLER_WIDTH),
                            Atom.newAtom(PER_SAMPLER_HEIGHT)
                    }
            );
            inlets[i] = patcher.newDefault(
                    i * 10,
                    0,
                    "inlet",
                    new Atom[]{});
            patcher.connect(inlets[i], 0, maxBoxes[i], 3);
            outlets[i] = patcher.newDefault(
                    i * 10,
                    100,
                    "outlet",
                    new Atom[]{});
            patcher.connect(maxBoxes[i], 0, outlets[i], 0);
        }
    }

    static int expectedWidth(BangSamplerArgs bangSamplerArgs) {
        return PER_SAMPLER_WIDTH;
    }

    static int expectedHeight(BangSamplerArgs bangSamplerArgs) {
        return (int) Math.ceil(bangSamplerArgs.getNumberOfSamples() * PER_SAMPLER_HEIGHT);
    }

    public static BPatcherSpecification getBPatcherSpecification(Atom[] args) {
        return new Specification(new BangSamplerArgs(args));
    }

    @AllArgsConstructor
    static class Specification implements BPatcherSpecification {
        private final BangSamplerArgs bangSamplerArgs;

        /**
         * The name of the bpatcher file (without maxpat suffix)
         */
        public String getBPatcherName() {
            return "bsmpmxj";
        }

        /**
         * The name of a child element of the bpatcher that can understand an initialization
         * message.
         */
        public String getNameOfInitializerMaxObject() {
            return "bsmpmxj";
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
            return bangSamplerArgs.toMaxArgs();
        }

        public int getWidth() {
            return expectedWidth(bangSamplerArgs);
        }

        public int getHeight() {
            return expectedHeight(bangSamplerArgs);
        }
    }
}
