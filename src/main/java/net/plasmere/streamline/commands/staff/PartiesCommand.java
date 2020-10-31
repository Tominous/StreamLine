package net.plasmere.streamline.commands.staff;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Party;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PartyUtils;

public class PartiesCommand extends Command {

    private final StreamLine plugin;

    public PartiesCommand(StreamLine streamLine, String perm, String[] aliases){
        super("parties", perm, aliases);
        this.plugin = streamLine;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (PartyUtils.getParties().size() <= 0) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.partiesNone);
            return;
        }
        for (Party party : PartyUtils.getParties()){
            MessagingUtils.sendBPUserMessage(party, sender, sender, MessageConfUtils.partiesMessage);
        }
    }
}
