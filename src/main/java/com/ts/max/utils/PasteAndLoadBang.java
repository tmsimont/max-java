package com.ts.max.utils;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxPatcher;

public final class PasteAndLoadBang {
    public static MaxBox forMaxBox(final MaxPatcher patcher,
                                   final MaxBox maxBox) {
        MaxBox pasteAndLoadBang = MaxPatcherUtils.newHidden(
                patcher,
                "js",
                // pasteAndLoadBang.js handles the actual work
                Atom.newAtom("pasteAndLoadBang.js"));
        pasteAndLoadBang.hide();
        patcher.connect(pasteAndLoadBang, 0, maxBox, 0);
        return pasteAndLoadBang;
    }
}
