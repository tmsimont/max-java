package com.ts.max.bpatcher;

import com.cycling74.max.Atom;

public interface BPatcherSpecification {
    /**
     * The name of the bpatcher file (without maxpat suffix)
     */
    String getBPatcherName();

    /**
     * The name of a child element of the bpatcher that can understand an initialization
     * message.
     */
    String getNameOfInitializerMaxObject();

    /**
     * The name of initializer method that the initializer max object will understand.
     */
    String getNameOfInitializerMethod();

    /**
     * The named initializer object will understand a message with these args.
     * The first arg in the list is the message name.
     */
    Atom[] getInitializerArgs();

    /**
     * The width that should be set for the BPatcher after it is constructed.
     */
    int getWidth();

    /**
     * The height that should be set for the BPatcher after it is constructed.
     */
    int getHeight();
}
