package net.plasmere.streamline.commands.staff;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.PluginUtils;
import net.plasmere.streamline.utils.TextUtils;

import java.util.ArrayList;

public class TeleportCommand extends Command implements TabExecutor {

    public TeleportCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = PlayerUtils.getPPlayer(args[0]);
            if (player == null) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer());
                return;
            }

            ServerInfo serverInfo = player.getServer().getInfo();

            ProxiedPlayer s = (ProxiedPlayer) sender;

            s.connect(serverInfo);

//            MessagingUtils.sendTeleportPluginMessageRequest(s, player);
            PlayerUtils.addTeleport(s, player);

            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bteleport()
                    .replace("%player%", player.getName())
            );
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers());
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            return TextUtils.getCompletion(PlayerUtils.getPlayerNamesForAllOnline(), args[0]);
        } else {
            return new ArrayList<>();
        }
    }
}
