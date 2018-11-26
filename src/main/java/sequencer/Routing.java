package sequencer;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxPatcher;
import utils.MaxPatcherUtils;

class Routing {
    private final MaxBox route;
    private final MaxBox router;

    Routing(final MaxPatcher patcher, final SequencerArgs args) {
        // Build a simple int array 1... number of outs
        int[] routeOuts = new int[args.beats];
        for (int i = 0; i < args.beats; i++) {
            routeOuts[i] = i + 1;
        }

        // Build a "route" with an output for each beat
        route = MaxPatcherUtils.newHidden(
                patcher,
                "route",
                Atom.newAtom(routeOuts));

        // Build a "router" with an input for each beat and an out for each voice.
        router = MaxPatcherUtils.newHidden(
                patcher,
                "router",
                Atom.newAtom(args.beats),
                Atom.newAtom(args.voices));

        // Connect route to router
        for (int i = 0; i < args.beats; i++) {
            patcher.connect(route, i, router, i + 1);
        }
    }

    MaxBox getVoiceRouter() {
        return router;
    }

    MaxBox getBeatInputRoute() {
        return route;
    }

    public void remove() {
        route.remove();
        router.remove();
    }
}
