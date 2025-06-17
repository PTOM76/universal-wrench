package net.pitan76.universalwrench;

import net.pitan76.mcpitanlib.api.util.CompatActionResult;

import java.util.function.Supplier;

public class WrenchAction {
    public Supplier<CompatActionResult> supplier;
    public int index = 0;

    public WrenchAction(Supplier<CompatActionResult> action) {
        this.supplier = action;
    }

    public WrenchAction(Supplier<CompatActionResult> action, int index) {
        this.supplier = action;
        this.index = index;
    }
}
