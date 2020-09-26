package uk.bobbytables.zenloot;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import uk.bobbytables.zenloot.commands.ZenLootCommand;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.VERSION,
        dependencies = "required-after:crafttweaker;after:zenstages"
)
public class ZenLootMod {
    @Mod.Instance(Reference.MOD_ID)
    public static ZenLootMod INSTANCE;

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ZenLootCommand());
    }
}
