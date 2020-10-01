package uk.bobbytables.zenloot.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.bobbytables.zenloot.ZenLootMod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static uk.bobbytables.zenloot.ZenLootMod.GSON_INSTANCE;

public class CommandDumpLoot extends CommandBase {
    @Override
    public String getName() {
        return "dump";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/zenloot dump";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Logger logger = LogManager.getLogger();
        boolean errorsOccurred = false;

        File outDir = new File(server.getDataDirectory(), "loot-tables");
        if (!outDir.exists()) {
            outDir.mkdir();
        }

        Set<ResourceLocation> allTables = new HashSet<>();
        allTables.addAll(LootTableList.getAll());
        allTables.addAll(ZenLootMod.UNREGISTERED_LOOT_TABLES);

        for (ResourceLocation tableId : allTables) {
            File resourceDir = new File(outDir, tableId.getNamespace());

            String[] fileName = tableId.getPath().split("/");
            if (fileName.length > 1) {
                String[] subDirs = Arrays.copyOfRange(fileName, 0, fileName.length - 1);
                for (String subDir : subDirs) {
                    resourceDir = new File(resourceDir, subDir);
                }
            }

            if (!resourceDir.exists()) resourceDir.mkdirs();

            File tableFile = new File(resourceDir, String.format("%s.json", fileName[fileName.length - 1]));

            LootTable table = server.getEntityWorld().getLootTableManager().getLootTableFromLocation(tableId);

            try (Writer writer = new FileWriter(tableFile)) {
                GSON_INSTANCE.toJson(table, table.getClass(), writer);
                writer.flush();
            } catch (IOException e) {
                errorsOccurred = true;
                logger.error("Encountered an error writing {}", tableFile.getPath());
                logger.error(e);
            }
        }

        sender.sendMessage(new TextComponentString("Loot tables have been dumped!"));
        if (errorsOccurred) {
            sender.sendMessage(new TextComponentString("Errors occurred"));
        }
    }
}
