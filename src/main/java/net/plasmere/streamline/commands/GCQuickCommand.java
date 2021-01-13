package net.plasmere.streamline.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.utils.TextUtils;

public class GCQuickCommand extends Command {
    private final StreamLine plugin;

    public GCQuickCommand(StreamLine streamLine, String perm, String[] aliases){
        super("gc", perm, aliases);
        this.plugin = streamLine;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.getProxy().getPluginManager().dispatchCommand(sender, "guild chat " + TextUtils.normalize(args));
    }
}
