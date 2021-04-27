package net.plasmere.streamline.commands.staff.spy;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;

public class GSPYCommand extends Command {
    public GSPYCommand(String perm, String[] aliases){
        super("gspy", perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            Player player = PlayerUtils.getStat(sender);

            if (player == null) return;

            player.toggleGSPY();

            StreamLine.getInstance().getLogger().info(String.valueOf(player.gspy));

            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.gspyToggle
                    .replace("%toggle%", (player.gspy ? MessageConfUtils.gspyOn : MessageConfUtils.gspyOff))
            );
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers);
        }
    }
}
