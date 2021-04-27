package net.plasmere.streamline.commands.staff;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.utils.GuildUtils;
import net.plasmere.streamline.utils.MessagingUtils;

public class GuildsCommand extends Command {

    private final StreamLine plugin;

    public GuildsCommand(StreamLine streamLine, String perm, String[] aliases){
        super("guilds", perm, aliases);
        this.plugin = streamLine;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (GuildUtils.getGuilds().size() <= 0) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.guildsNone);
            return;
        }

        for (Guild guild : GuildUtils.getGuilds()){
            MessagingUtils.sendBGUserMessage(guild, sender, sender, MessageConfUtils.guildsMessage);
        }
    }
}
