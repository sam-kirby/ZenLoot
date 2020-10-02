package uk.bobbytables.zenloot.crafttweaker.zenscript;

import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

public abstract class MCLootPool {
    private final String name;
    protected final List<MCLootEntry> lootEntries = new ArrayList<>();
    protected RandomValueRange rolls = null;
    protected RandomValueRange bonusRolls = null;

    public MCLootPool(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @ZenMethod
    public MCLootPool addEntry(MCLootEntry lootEntry) {
        this.lootEntries.add(lootEntry);
        return this;
    }

    public abstract void process(LootTable table);
}
