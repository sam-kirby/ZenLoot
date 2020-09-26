package uk.bobbytables.zenloot.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

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

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer())
                .registerTypeAdapter(LootPool.class, new LootPool.Serializer())
                .registerTypeAdapter(LootTable.class, new LootTable.Serializer())
                .registerTypeHierarchyAdapter(LootEntry.class, new LootEntry.Serializer())
                .registerTypeHierarchyAdapter(LootFunction.class, new LootFunctionManager.Serializer())
                .registerTypeHierarchyAdapter(LootCondition.class, new LootConditionManager.Serializer())
                .registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer())
                .setPrettyPrinting()
                .create();

        for (ResourceLocation tableId : LootTableList.getAll()) {
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
                gson.toJson(table, table.getClass(), writer);
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
