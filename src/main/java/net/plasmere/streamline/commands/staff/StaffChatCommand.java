package net.plasmere.streamline.commands.staff;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.DiscordMessage;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class StaffChatCommand extends Command {
    private final StreamLine plugin;
    private final Configuration config = Config.getConf();

    public StaffChatCommand(StreamLine streamLine, String perm, String[] aliases){
        super("staffchat", perm, aliases);
        this.plugin = streamLine;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (ConfigUtils.moduleStaffChat)
            if (sender.hasPermission("streamline.staff.staffchat")) {
                MessagingUtils.sendStaffMessage(sender, MessageConfUtils.bungeeStaffChatFrom, TextUtils.concat(args), plugin);
                MessagingUtils.sendDiscordEBMessage(new DiscordMessage(sender, MessageConfUtils.staffChatEmbedTitle, TextUtils.concat(args), ConfigUtils.textChannelStaffChat));
            } else
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.prefix + MessageConfUtils.noPerm);
    }
}
