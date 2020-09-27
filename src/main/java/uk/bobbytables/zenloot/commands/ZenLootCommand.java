package uk.bobbytables.zenloot.commands;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class ZenLootCommand extends CommandTreeBase {
    public ZenLootCommand() {
        this.addSubcommand(new CommandDumpLoot());
        this.addSubcommand(new CommandChest());
    }

    @Override
    public String getName() {
        return "zenloot";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }
}
