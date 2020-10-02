package uk.bobbytables.zenloot.crafttweaker.zenscript;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.zenloot.LootPoolExisting")
public class MCLootPoolExisting extends MCLootPool {
    public MCLootPoolExisting(String name) {
        super(name);
    }

    @Override
    public void process(LootTable table) {
        LootPool pool = table.getPool(this.getName());
        if (pool == null) {
            CraftTweakerAPI.logError("Tried to modify a pool that doesn't exist");
            return;
        }
        for (MCLootEntry lootEntry : this.lootEntries) {
            pool.addEntry(lootEntry.build());
        }
        if (this.rolls != null) {
            pool.setRolls(this.rolls);
        }
        if (this.bonusRolls != null) {
            pool.setBonusRolls(this.bonusRolls);
        }
    }
}
