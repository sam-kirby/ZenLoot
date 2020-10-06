package uk.bobbytables.zenloot.crafttweaker.zenscript;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import stanhebben.zenscript.annotations.ZenClass;

import java.lang.reflect.Field;
import java.util.List;

@ZenRegister
@ZenClass("mods.zenloot.LootPoolExisting")
public class MCLootPoolExisting extends MCLootPool {
    private static final Field poolConditionsField = ObfuscationReflectionHelper.findField(LootPool.class, "field_186454_b");
    private Boolean clearFlag = false;
    private RandomValueRange rolls = null;
    private RandomValueRange bonusRolls = null;

    public MCLootPoolExisting(String name) {
        super(name);
    }

    @Override
    public void process(LootTable table, ResourceLocation tableLoc) {
        LootPool pool = table.getPool(this.getName());
        if (pool == null) {
            CraftTweakerAPI.logError(String.format("Tried to modify a pool (%s) that doesn't exist in table %s", this.getName(), tableLoc));
            return;
        }

        if (clearFlag) {
            table.removePool(this.getName());
            try {
                pool = new LootPool(new LootEntry[]{}, ((List<LootCondition>) poolConditionsField.get(pool)).toArray(new LootCondition[]{}), pool.getRolls(), pool.getBonusRolls(), this.getName());
            } catch (IllegalAccessException e) {
                CraftTweakerAPI.logError("Error retrieving pool conditions");
                pool = new LootPool(new LootEntry[]{}, new LootCondition[]{}, pool.getRolls(), pool.getBonusRolls(), this.getName());
            }

            table.addPool(pool);
        }

        if (this.rolls != null) pool.setRolls(this.rolls);
        if (this.bonusRolls != null) pool.setBonusRolls(this.bonusRolls);

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

    @Override
    public MCLootPool clear() {
        this.clearFlag = true;
        return super.clear();
    }

    @Override
    public MCLootPool setRolls(int min, int max) {
        this.rolls = new RandomValueRange(min, max);
        return super.setRolls(min, max);
    }

    @Override
    public MCLootPool setBonusRolls(int min, int max) {
        this.bonusRolls = new RandomValueRange(min, max);
        return super.setBonusRolls(min, max);
    }
}
