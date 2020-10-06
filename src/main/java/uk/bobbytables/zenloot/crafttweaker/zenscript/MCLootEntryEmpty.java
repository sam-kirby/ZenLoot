package uk.bobbytables.zenloot.crafttweaker.zenscript;

import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class MCLootEntryEmpty extends MCLootEntry {
    public MCLootEntryEmpty(int weight) {
        super("empty");
        this.weight = weight;
    }

    @Override
    public LootEntry build() {
        return new LootEntryEmpty(this.weight, this.quality, this.lootConditions.toArray(lootConditions.toArray(new LootCondition[]{})), this.name);
    }
}
