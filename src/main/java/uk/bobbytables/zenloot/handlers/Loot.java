package uk.bobbytables.zenloot.handlers;

import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import uk.bobbytables.zenloot.ZenLootMod;
import uk.bobbytables.zenloot.crafttweaker.zenscript.MCLootTable;

@Mod.EventBusSubscriber
public class Loot {
    @SubscribeEvent
    public static void catchUnregisteredTables(LootTableLoadEvent event) {
        if (!LootTableList.getAll().contains(event.getName())) {
            ZenLootMod.UNREGISTERED_LOOT_TABLES.add(event.getName());
            LogManager.getLogger().warn("Unregistered loot table detected: {}", event.getName());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLootTableLoadEarly(LootTableLoadEvent event) {
        MCLootTable.MODIFIED_EARLY.stream()
                .filter(mcLootTable -> mcLootTable.matches(event.getName()))
                .findFirst()
                .ifPresent(mcLootTable -> mcLootTable.getLootPools().forEach(mcLootPool -> mcLootPool.process(event.getTable(), event.getName())));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLootTableLoad(LootTableLoadEvent event) {
        MCLootTable.MODIFIED_TABLES.stream()
                .filter(mcLootTable -> mcLootTable.matches(event.getName()))
                .findFirst()
                .ifPresent(mcLootTable -> mcLootTable.getLootPools().forEach(mcLootPool -> mcLootPool.process(event.getTable(), event.getName())));
    }
}
