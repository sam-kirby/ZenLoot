package uk.bobbytables.zenloot.crafttweaker.zenscript;

import crafttweaker.annotations.ZenRegister;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.zenloot.LootPoolNew")
public class MCLootPoolNew extends MCLootPool {
    public MCLootPoolNew(String name, RandomValueRange rolls, RandomValueRange bonusRolls) {
        super(name);
        this.rolls = rolls;
        this.bonusRolls = bonusRolls;
    }

    @Override
    public void process(LootTable table) {
        table.addPool(new LootPool(
                this.lootEntries.stream().map(MCLootEntry::build).toArray(LootEntry[]::new),
                new LootCondition[]{},
                this.rolls,
                this.bonusRolls,
                this.getName()
        ));
    }
}
