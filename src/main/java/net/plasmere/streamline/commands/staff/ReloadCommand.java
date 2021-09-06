package net.plasmere.streamline.commands.staff;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {
    private String perm = "";

    public ReloadCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);

        this.perm = perm;
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (sender.hasPermission(perm)) {
            try {
                StreamLine.config.reloadConfig();
                StreamLine.config.reloadLocales();
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.prefix() + MessageConfUtils.reload());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.prefix() + MessageConfUtils.noPerm());
        }
    }
}
