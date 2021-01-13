package net.plasmere.streamline.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.utils.TextUtils;

public class PCQuickCommand extends Command {
    private final StreamLine plugin;

    public PCQuickCommand(StreamLine streamLine, String perm, String[] aliases){
        super("pc", perm, aliases);
        this.plugin = streamLine;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.getProxy().getPluginManager().dispatchCommand(sender, "party chat " + TextUtils.concat(args));
    }
}
