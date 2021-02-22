package com.ts.max.sequencer;

import com.cycling74.max.MaxPatcher;

class Sequencer {
    private final MaxPatcher patcher;
    private final SequencerArgs args;
    private final VisualControls controls;
    private final Routing routing;
    private final CountDriver driver;
    private final VoiceOutletOutputs outletOutputs;
    private final VoiceSendOutputs sendOutputs;

    Sequencer(MaxPatcher patcher,
                     SequencerArgs args) {
        this.patcher = patcher;
        this.args = args;

        // Build individual components.
        routing = new Routing(patcher, args);
        controls = new VisualControls(patcher, args);
        driver = new CountDriver(patcher, args);
        outletOutputs = new VoiceOutletOutputs(patcher, args);
        sendOutputs = new VoiceSendOutputs(patcher, args);

        // Wire up the individual components.
        wireUp();
    }

    private void wireUp() {
        patcher.connect(driver.getSignalDriver(), 0, routing.getBeatInputRoute(), 0);
        patcher.connect(controls.getRoutingControl(), 0, routing.getVoiceRouter(), 0);
        for (int i = 0; i < args.getNumberOfVoices(); i++) {
            patcher.connect(routing.getVoiceRouter(), i, outletOutputs.get(i), 0);
            patcher.connect(routing.getVoiceRouter(), i, sendOutputs.get(i), 0);
        }
    }
}
