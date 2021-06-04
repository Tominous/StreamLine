package net.plasmere.streamline.commands.staff;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Party;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PartyUtils;
import net.plasmere.streamline.utils.PlayerUtils;

public class GetStatsCommand extends Command {
    public GetStatsCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (PlayerUtils.getStats().size() <= 0) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.getStatsNone);
            return;
        }

        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.getStatsMessage
                .replace("%stats%", getStats())
        );
    }

    public static String getStats() {
        StringBuilder stringBuilder = new StringBuilder();

        int i = 1;
        for (Player stat : PlayerUtils.getStats()) {
            if (i >= PlayerUtils.getStats().size()) {
                stringBuilder.append(MessageConfUtils.getStatsLast.replace("%player%", PlayerUtils.getOffOnDisplayBungee(stat)));
            } else {
                stringBuilder.append(MessageConfUtils.getStatsNLast.replace("%player%", PlayerUtils.getOffOnDisplayBungee(stat)));
            }
            i ++;
        }

        return stringBuilder.toString();
    }
}
