package net.plasmere.streamline.commands.staff.spy;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;

public class SCViewCommand extends Command {

    public SCViewCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            Player player = PlayerUtils.getStat(sender);

            if (player == null) return;

            player.toggleSCView();

            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.scViewToggle
//                    .replace("%toggle%", (player.viewsc ? "&aON" : "&cOFF"))
                    .replace("%toggle%", (player.viewsc ? MessageConfUtils.scViewOn : MessageConfUtils.scViewOff))
            );
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers);
        }
    }
}
