import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import utils.MaxBoxBuilder;

public class bsmp extends MaxBoxBuilder {

    public bsmp(Atom[] args) {
        super(args);
    }

    @Override
    public MaxBox[] build(Atom[] args) {
        if (args.length != 3) {
            bail("Usage: bsmp [recv/send name] [number of samples] [starting index]");
        }
        try {
            String prefix = args[0].getString();
            int num = args[1].getInt();
            int pidx = args[2].getInt();
            MaxBox[] boxes = new MaxBox[num];
            for (int i = 0; i < num; i++) {
                String name = prefix + (pidx + i);
                String signalName = "signal-" + name;
                String bufferName = "buffer-" + name;
                boxes[i] = this.getParentPatcher().newDefault(
                        0,
                        0,
                        "bpatcher",
                        new Atom[]{
                                Atom.newAtom("TxtBangSamp"),
                                Atom.newAtom("@args"),
                                Atom.newAtom(bufferName),
                                Atom.newAtom(signalName),
                                Atom.newAtom(name),
                                Atom.newAtom(prefix),
                                Atom.newAtom("@embed"),
                                Atom.newAtom(1),
                        }
                );
            }
            return boxes;
        } catch (Throwable e) {
            bail("Usage: bsmp [recv/send name] [number of samples] [starting index]");
            return null;
        }
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
                x + 350,
                y + 70
        };
    }
}
