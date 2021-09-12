package net.plasmere.streamline.commands.staff;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.utils.MessagingUtils;

public class InfoCommand extends Command {

    public InfoCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        MessagingUtils.sendInfo(sender);
    }
}
