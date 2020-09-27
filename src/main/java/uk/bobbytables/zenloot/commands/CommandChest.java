package uk.bobbytables.zenloot.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandChest extends CommandBase {
    @Override
    public String getName() {
        return "chest";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/zenloot chest <loot_table>";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, LootTableList.getAll()) : Collections.emptyList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1 && LootTableList.getAll().contains(new ResourceLocation(args[0]))) {
            server.getCommandManager().executeCommand(sender, String.format("give @p minecraft:chest 1 0 {BlockEntityTag:{LootTable:\"%s\"}}", args[0]));
        } else {
            sender.sendMessage(new TextComponentString("No such loot table"));
        }
    }
}
