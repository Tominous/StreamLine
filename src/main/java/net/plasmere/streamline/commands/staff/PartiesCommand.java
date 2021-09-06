package net.plasmere.streamline.commands.staff;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Party;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PartyUtils;

public class PartiesCommand extends Command {
    public PartiesCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (PartyUtils.getParties().size() <= 0) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.partiesNone());
            return;
        }
        for (Party party : PartyUtils.getParties()){
            MessagingUtils.sendBPUserMessage(party, sender, sender, MessageConfUtils.partiesMessage());
        }
    }
}
