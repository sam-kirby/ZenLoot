package uk.bobbytables.zenloot.crafttweaker.zenscript;

import crafttweaker.annotations.ZenRegister;
import net.minecraft.world.storage.loot.functions.LootFunction;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import uk.bobbytables.zenloot.ZenLootMod;

@ZenRegister
@ZenClass("mods.zenloot.LootFunction")
public class MCLootFunction {
    private final LootFunction inner;

    public MCLootFunction(LootFunction inner) {
        this.inner = inner;
    }

    public LootFunction getInner() {
        return this.inner;
    }

    @ZenMethod
    public static MCLootFunction parse(String jsonString) {
        return new MCLootFunction(ZenLootMod.GSON_INSTANCE.fromJson(jsonString, LootFunction.class));
    }
}
