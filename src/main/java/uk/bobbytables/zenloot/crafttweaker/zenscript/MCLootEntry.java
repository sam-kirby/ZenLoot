package uk.bobbytables.zenloot.crafttweaker.zenscript;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.darkhax.itemstages.ItemStages;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import uk.bobbytables.zenloot.loot.conditions.Staged;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass("mods.zenloot.LootEntry")
public class MCLootEntry {
    private final Item item;
    private final ItemStack itemStack;
    private final List<LootCondition> lootConditions = new ArrayList<>();
    private final List<LootFunction> lootFunctions = new ArrayList<>();
    private final String name;
    private int weight;
    private int quality;
    private boolean autoStage = false;

    MCLootEntry(ItemStack itemStack, String name) {
        if (itemStack.getCount() > 1) {
            this.lootFunctions.add(new SetCount(new LootCondition[]{}, new RandomValueRange(itemStack.getCount())));
        }
        if (itemStack.hasTagCompound()) {
            this.lootFunctions.add(new SetNBT(new LootCondition[]{}, itemStack.getTagCompound()));
        }
        if (itemStack.getMetadata() != 0) {
            this.lootFunctions.add(new SetMetadata(new LootCondition[]{}, new RandomValueRange(itemStack.getMetadata())));
        }
        this.item = itemStack.getItem();
        this.itemStack = itemStack;
        this.name = name;
    }

    @ZenMethod
    public static MCLootEntry fromItemStack(IItemStack iItemStack, @Optional String name) {
        return new MCLootEntry(CraftTweakerMC.getItemStack(iItemStack), name);
    }

    public LootEntryItem build() {
        if (this.autoStage) {
            this.lootConditions.add(new Staged(ItemStages.getStage(itemStack)));
        }
        return new LootEntryItem(
                this.item,
                this.weight,
                this.quality,
                this.lootFunctions.toArray(new LootFunction[]{}),
                this.lootConditions.toArray(new LootCondition[]{}),
                this.name == null ? this.item.getRegistryName().toString() : this.name
        );
    }

    @ZenMethod
    public MCLootEntry setDamage(float min, float max) {
        if (this.lootFunctions.stream().noneMatch(lootFunction -> lootFunction.getClass().equals(SetDamage.class))) {
            if (min < max && max <= 1.0 && min > 0.0) {
                this.lootFunctions.add(new SetDamage(new LootCondition[]{}, new RandomValueRange(min, max)));
            } else {
                CraftTweakerAPI.logError(String.format("Tried to set an invalid damage for %s - must be 0.0 - 1.0", this.item.getRegistryName()));
            }
        } else
            CraftTweakerAPI.logError(String.format("Tried to setDamage twice for %s", this.item.getRegistryName()));
        return this;
    }

    @ZenMethod
    public MCLootEntry setCount(int min, int max) {
        if (this.lootFunctions.stream().noneMatch(lootFunction -> lootFunction.getClass().equals(SetCount.class)))
            this.lootFunctions.add(new SetCount(new LootCondition[]{}, new RandomValueRange(min, max)));
        else
            CraftTweakerAPI.logError(String.format("Tried to setCount twice for %s", this.item.getRegistryName()));
        return this;
    }

    @ZenMethod
    public MCLootEntry setStage(@Optional String stageName) {
        if (stageName == null) {
            this.autoStage = true;
        } else {
            this.lootConditions.add(new Staged(stageName));
        }
        return this;
    }

    @ZenMethod
    public MCLootEntry setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    @ZenMethod
    public MCLootEntry setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    @ZenMethod
    public MCLootEntry addCondition(MCLootCondition lootCondition) {
        throw new UnsupportedOperationException();
        // return this;
    }

    @ZenMethod
    public MCLootEntry addFunction(MCLootFunction lootFunction) {
        throw new UnsupportedOperationException();
        // return this
    }
}
