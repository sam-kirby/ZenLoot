package uk.bobbytables.zenloot.crafttweaker.zenscript;

import crafttweaker.annotations.ZenRegister;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.RandomValueRange;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import uk.bobbytables.zenloot.util.tablematchers.PatternTableMatcher;
import uk.bobbytables.zenloot.util.tablematchers.SimpleTableMatcher;
import uk.bobbytables.zenloot.util.tablematchers.TableMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ZenRegister
@ZenClass("mods.zenloot.LootTable")
public class MCLootTable {
    public static final List<MCLootTable> MODIFIED_TABLES = new ArrayList<>();
    public static final List<MCLootTable> MODIFIED_EARLY = new ArrayList<>();

    private final TableMatcher matcher;
    private final List<MCLootPool> lootPools = new ArrayList<>();

    public MCLootTable(ResourceLocation id) {
        this.matcher = new SimpleTableMatcher(id);
    }

    public MCLootTable(String pattern) {
        this.matcher = new PatternTableMatcher(pattern);
    }

    public boolean matches(ResourceLocation tableLoc) {
        return this.matcher.matches(tableLoc);
    }

    public List<MCLootPool> getLootPools() {
        return lootPools;
    }

    @ZenMethod
    public static MCLootTable edit(String id) {
        MCLootTable lootTable = new MCLootTable(new ResourceLocation(id));
        MODIFIED_TABLES.add(lootTable);
        return lootTable;
    }

    @ZenMethod
    public static MCLootTable editEarly(String id) {
        MCLootTable lootTable = new MCLootTable(new ResourceLocation(id));
        MODIFIED_EARLY.add(lootTable);
        return lootTable;
    }

    @ZenMethod
    public static MCLootTable editAllMatching(String pattern) {
        MCLootTable lootTable = new MCLootTable(pattern);
        MODIFIED_TABLES.add(lootTable);
        return lootTable;
    }

    @ZenMethod
    public MCLootPool getExistingPool(String name) {
        Optional<MCLootPool> lootPool = lootPools.stream().filter(pool -> pool.getName().equals(name)).findFirst();
        if (lootPool.isPresent()) return lootPool.get();
        MCLootPool lootPool1 = new MCLootPoolExisting(name);
        lootPools.add(lootPool1);
        return lootPool1;
    }

    @ZenMethod
    public MCLootPool addPool(String name, int minRolls, int maxRolls, int minBonusRolls, int maxBonusRolls) {
        Optional<MCLootPool> lootPool = lootPools.stream().filter(pool -> pool.getName().equals(name)).findFirst();
        if (lootPool.isPresent()) return lootPool.get();
        MCLootPool lootPool1 = new MCLootPoolNew(name, new RandomValueRange(minRolls, maxRolls), new RandomValueRange(minBonusRolls, maxBonusRolls));
        lootPools.add(lootPool1);
        return lootPool1;
    }
}
