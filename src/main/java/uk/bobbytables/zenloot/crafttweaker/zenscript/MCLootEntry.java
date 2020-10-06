package uk.bobbytables.zenloot.crafttweaker.zenscript;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass("mods.zenloot.LootEntry")
public abstract class MCLootEntry {
    protected final List<LootCondition> lootConditions = new ArrayList<>();
    protected final String name;
    protected int weight = 1;
    protected int quality = 0;

    public MCLootEntry(String name) {
        this.name = name;
    }

    public abstract LootEntry build();

    @ZenMethod
    public static MCLootEntryItem fromItemStack(IItemStack iItemStack, @Optional String name) {
        return new MCLootEntryItem(CraftTweakerMC.getItemStack(iItemStack), name);
    }

    @ZenMethod
    public static MCLootEntryEmpty empty(int weight) {
        return new MCLootEntryEmpty(weight);
    }
}
