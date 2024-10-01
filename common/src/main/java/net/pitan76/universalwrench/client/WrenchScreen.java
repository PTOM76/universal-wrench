package net.pitan76.universalwrench.client;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.pitan76.mcpitanlib.api.client.gui.screen.CompatInventoryScreen;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.universalwrench.screen.WrenchEditTableScreenHandler;

public class WrenchScreen extends CompatInventoryScreen<WrenchEditTableScreenHandler> {
    private static final CompatIdentifier TEXTURE = CompatIdentifier.of("universalwrench", "textures/gui/wrench_edit_table.png");

    public WrenchScreen(WrenchEditTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        setBackgroundWidth(176);
        setBackgroundHeight(166);
    }

    @Override
    public CompatIdentifier getCompatTexture() {
        return TEXTURE;
    }
}
