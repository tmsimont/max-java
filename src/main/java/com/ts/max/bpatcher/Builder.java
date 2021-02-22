package com.ts.max.bpatcher;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxClock;
import com.cycling74.max.MaxObject;
import com.ts.max.bsmp.BangSamplerMxj;
import com.ts.max.sequencer.SequencerMxj;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class Builder extends MaxObject {
    private final Lock lock = new ReentrantLock();
    private final MaxClock cl;
    private Queue<Runnable> callbacks = new ArrayDeque<>();

    public Builder() {
        cl = new MaxClock(this, "pollCallback");
    }

    private void make(BPatcherSpecification specification) {
        try {
            lock.lock();
            Random r = new Random(System.currentTimeMillis());
            long id = r.nextLong();
            String varName = "r-" + id;
            sendMakeCommandToJSLayer(specification, varName);
            registerBPatcherInitializerAsCallback(specification, varName);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Needs to be connected to a max object with maker.js loaded.
     * @param args
     */
    public void makeSequencer(Atom[] args) {
        make(SequencerMxj.getBPatcherSpecification(args));
    }

    public void makeBangSampler(Atom[] args) {
        make(BangSamplerMxj.getBPatcherSpecification(args));
    }

    private void sendMakeCommandToJSLayer(
            final BPatcherSpecification bPatcherSpecification,
            final String varName) {
        // Build a list args that are required to get an embedded bpatcher that we can find by name.
        Atom[] bpatcherArgs = new Atom[] {
                Atom.newAtom(0),
                Atom.newAtom(0),
                Atom.newAtom("bpatcher"),
                // This is the actual file name (bpatcherName.maxpat)
                Atom.newAtom(bPatcherSpecification.getBPatcherName()),
                Atom.newAtom("@embed"),
                Atom.newAtom(1),
                // This is the name we use to get the bpatcher with getNamed()
                Atom.newAtom("@varname"),
                Atom.newAtom(varName)
        };

        // Send a make command with all the bpatcher args.
        outlet(0, "make", bpatcherArgs);
    }

    private void registerBPatcherInitializerAsCallback(
            final BPatcherSpecification bPatcherSpecification,
            final String varName) {
        // Get the args we need to call a given initializer in our new bpatcher instance
        Atom[] initializerRoutingArgs = new Atom[] {
                Atom.newAtom(varName),
                // The varname of the object we expect to be present in the patcher (maxpat file)
                Atom.newAtom(bPatcherSpecification.getNameOfInitializerMaxObject()),
                // The method name this object should understand
                Atom.newAtom(bPatcherSpecification.getNameOfInitializerMethod())
        };
        Atom[] completeInitializationArgs = Stream.concat(
                Arrays.stream(initializerRoutingArgs),
                // The arguments the method expects
                Arrays.stream(bPatcherSpecification.getInitializerArgs()))
                .toArray(Atom[]::new);

        // Register a callback that will be invoked after a delay.
        callbacks.add(() -> {
            // Initialize the bpatcher subpatcher
            outlet(0, "message_named_child_of_bpatcher", completeInitializationArgs);

            // Resize the bpatcher
            outlet(0, "resize_bpatcher",
                    new Atom[]{
                            Atom.newAtom(varName),
                            Atom.newAtom(bPatcherSpecification.getWidth()),
                            Atom.newAtom(bPatcherSpecification.getHeight()),
                    });
        });

        // Delay time shouldn't matter since the callback will end up being blocked by lock acquisition.
        // This works because the call out to the JS layer happens in the same thread.
        cl.delay(1.);
    }

    public void pollCallback() {
        try {
            lock.lock();
            callbacks.poll().run();
        } finally {
            lock.unlock();
        }
    }

    public void notifyDeleted() {
        cl.release();
    }
}
