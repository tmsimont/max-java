import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxPatcher;
import sequencer.Sequencer;
import sequencer.SequencerArgs;
import utils.MaxBoxBuilder;

public class seq extends MaxBoxBuilder {
    private SequencerArgs sequencerArgs;

    public seq(Atom[] args) {
        super(args);
    }

    @Override
    public MaxBox[] build(Atom[] args) {
        if (args.length != 4) {
            bail("Usage: seq [voices] [beats] [send name] [receive name]");
        }
        MaxPatcher patcher = this.getParentPatcher();
        sequencerArgs = new SequencerArgs();
        try {
            sequencerArgs.x = 0;
            sequencerArgs.y = 0;
            sequencerArgs.voices = args[0].getInt();
            sequencerArgs.beats = args[1].getInt();
            sequencerArgs.sendName = args[2].getString();
            sequencerArgs.recvName = args[3].getString();
        } catch (Throwable e) {
            bail("Usage: seq [voices] [beats] [send name] [receive name]");
        }

        MaxBox box = patcher.newDefault(
                0,
                0,
                "bpatcher",
                new Atom[]{
                        Atom.newAtom("seqmxj"),
                        Atom.newAtom("@embed"),
                        Atom.newAtom(1)
                }
        );

        MaxPatcher seqPatcher = box.getSubPatcher().getNamedBox("seqmxj").getPatcher();
        new Sequencer(seqPatcher, sequencerArgs);
        return new MaxBox[]{box};
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    public int[] getRect(int x, int y) {
        return new int[] {
                x,
                y,
                x + (25 + (int) (16.125 * sequencerArgs.beats)),
                y + (25 + (int) (16.6666 * sequencerArgs.voices))
        };
    }
}

