package net.plasmere.streamline.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.utils.TextUtils;

public class PCQuickCommand extends Command {
    public PCQuickCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        StreamLine.getInstance().getProxy().getPluginManager().dispatchCommand(sender, "party chat " + TextUtils.normalize(args));
    }
}
