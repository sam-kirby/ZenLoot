package uk.bobbytables.zenloot.crafttweaker.zenscript;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

public abstract class MCLootPool {
    protected final List<MCLootEntry> lootEntries = new ArrayList<>();
    private final String name;
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

    @ZenMethod
    public MCLootPool clear() {
        this.lootEntries.clear();
        return this;
    }

    @ZenMethod
    public MCLootPool setRolls(int min, int max) {
        return this;
    }

    @ZenMethod
    public MCLootPool setBonusRolls(int min, int max) {
        return this;
    }

    public abstract void process(LootTable table, ResourceLocation tableLoc);
}
