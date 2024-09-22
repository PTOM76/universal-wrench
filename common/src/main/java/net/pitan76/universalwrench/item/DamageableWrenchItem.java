package net.pitan76.universalwrench.item;

import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.DefaultItemGroups;
import net.pitan76.mcpitanlib.api.item.ExtendItem;

public class DamageableWrenchItem extends WrenchItem {
    public DamageableWrenchItem(CompatibleItemSettings settings) {
        super(settings);
    }

    public DamageableWrenchItem(int damage) {
        this(CompatibleItemSettings.of().maxDamage(damage).addGroup(DefaultItemGroups.TOOLS));
    }
}
