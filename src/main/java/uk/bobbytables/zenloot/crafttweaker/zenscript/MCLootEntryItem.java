package uk.bobbytables.zenloot.crafttweaker.zenscript;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.darkhax.itemstages.ItemStages;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.EnchantRandomly;
import net.minecraft.world.storage.loot.functions.EnchantWithLevels;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import org.apache.logging.log4j.LogManager;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import uk.bobbytables.zenloot.loot.conditions.Staged;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass("mods.zenloot.LootEntryItem")
public class MCLootEntryItem extends MCLootEntry {
    private final Item item;
    private final ItemStack itemStack;
    private final List<LootFunction> lootFunctions = new ArrayList<>();
    private boolean autoStage = false;

    MCLootEntryItem(ItemStack itemStack, String name) {
        super(name);

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
    }

    @ZenMethod
    public static MCLootEntryItem fromItemStack(IItemStack iItemStack, @Optional String name) {
        return new MCLootEntryItem(CraftTweakerMC.getItemStack(iItemStack), name);
    }

    public LootEntry build() {
        if (this.autoStage) {
            String stage = ItemStages.getStage(itemStack);
            if (stage != null) {
                LogManager.getLogger().info("{} adding to stage: {}", itemStack.getItem().getRegistryName(), stage);
                this.lootConditions.add(new Staged(stage));
                this.autoStage = false; // prevents adding additional conditions each time game is loaded
            } else {
                if (Loader.instance().getLoaderState() == LoaderState.SERVER_ABOUT_TO_START || Loader.instance().getLoaderState() == LoaderState.SERVER_STARTING || Loader.instance().getLoaderState() == LoaderState.SERVER_STARTED) {
                    CraftTweakerAPI.logError(String.format("Tried to autostage an item which is not staged: %s", itemStack.getItem().getRegistryName()));
                } else {
                    CraftTweakerAPI.logWarning(String.format("Loot tables are being loaded before staging is finalised, skipping staging %s until later", itemStack.getItem().getRegistryName()));
                }
            }
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
    public MCLootEntryItem setDamage(float min, float max) {
        if (this.lootFunctions.stream().noneMatch(lootFunction -> lootFunction.getClass().equals(SetDamage.class))) {
            if (min < max && max <= 1.0 && min >= 0.0) {
                this.lootFunctions.add(new SetDamage(new LootCondition[]{}, new RandomValueRange(min, max)));
            } else {
                CraftTweakerAPI.logError(String.format("Tried to set an invalid damage for %s - must be 0.0 - 1.0", this.item.getRegistryName()));
            }
        } else
            CraftTweakerAPI.logError(String.format("Tried to setDamage twice for %s", this.item.getRegistryName()));
        return this;
    }

    @ZenMethod
    public MCLootEntryItem setCount(int min, int max) {
        if (this.lootFunctions.stream().noneMatch(lootFunction -> lootFunction.getClass().equals(SetCount.class)))
            this.lootFunctions.add(new SetCount(new LootCondition[]{}, new RandomValueRange(min, max)));
        else
            CraftTweakerAPI.logError(String.format("Tried to setCount twice for %s", this.item.getRegistryName()));
        return this;
    }

    @ZenMethod
    public MCLootEntryItem setStage(@Optional String stageName) {
        if (stageName == null) {
            this.autoStage = true;
        } else {
            this.lootConditions.add(new Staged(stageName));
        }
        return this;
    }

    @ZenMethod
    public MCLootEntryItem setChance(float chance) {
        this.lootConditions.add(new RandomChance(chance));
        return this;
    }

    @ZenMethod
    public MCLootEntryItem addRandomEnchant() {
        this.lootFunctions.add(new EnchantRandomly(new LootCondition[]{}, new ArrayList<>()));
        return this;
    }

    @ZenMethod
    public MCLootEntryItem addEnchantWithLevels(int levels, boolean treasure) {
        this.lootFunctions.add(new EnchantWithLevels(new LootCondition[]{}, new RandomValueRange(levels, levels), treasure));
        return this;
    }

    @ZenMethod
    public MCLootEntryItem addEnchantWithLevels(int minLevels, int maxLevels, boolean treasure) {
        this.lootFunctions.add(new EnchantWithLevels(new LootCondition[]{}, new RandomValueRange(minLevels, maxLevels), treasure));
        return this;
    }

    @ZenMethod
    public MCLootEntryItem setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    @ZenMethod
    public MCLootEntryItem setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    @ZenMethod
    public MCLootEntryItem addCondition(MCLootCondition lootCondition) {
        throw new UnsupportedOperationException();
        // return this;
    }

    @ZenMethod
    public MCLootEntryItem addFunction(MCLootFunction lootFunction) {
        this.lootFunctions.add(lootFunction.getInner());
        return this;
    }
}
