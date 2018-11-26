package sequencer;

import com.cycling74.max.MaxPatcher;

public class Sequencer {
    private final MaxPatcher patcher;
    private final SequencerArgs args;
    private final VisualControls controls;
    private final Routing routing;
    private final CountDriver driver;
    private final VoiceSendOutputs outputs;

    public Sequencer(MaxPatcher patcher,
                     SequencerArgs args) {
        this.patcher = patcher;
        this.args = args;

        // Build individual components.
        routing = new Routing(patcher, args);
        controls = new VisualControls(patcher, args);
        driver = new CountDriver(patcher, args);
        outputs = new VoiceSendOutputs(patcher, args);

        // Wire up the individual components.
        wireUp();
    }

    private void wireUp() {
        patcher.connect(driver.getSignalDriver(), 0, routing.getBeatInputRoute(), 0);
        patcher.connect(controls.getRoutingControl(), 0, routing.getVoiceRouter(), 0);
        for (int i = 0; i < args.voices; i++) {
            patcher.connect(routing.getVoiceRouter(), i, outputs.get(i), 0);
        }
    }
}
