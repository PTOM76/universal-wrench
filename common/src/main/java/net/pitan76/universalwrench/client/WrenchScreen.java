package net.pitan76.universalwrench.client;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.pitan76.mcpitanlib.api.client.CompatInventoryScreen;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;

public class WrenchScreen extends CompatInventoryScreen {
    private static final CompatIdentifier TEXTURE = CompatIdentifier.of("universalwrench", "textures/gui/wrench_edit_table.json.png");

    public WrenchScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        setBackgroundWidth(176);
        setBackgroundHeight(166);
    }

    @Override
    public CompatIdentifier getCompatTexture() {
        return TEXTURE;
    }
}
