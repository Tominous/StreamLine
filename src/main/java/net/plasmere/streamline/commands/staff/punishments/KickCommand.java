package net.plasmere.streamline.commands.staff.punishments;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KickCommand extends Command implements TabExecutor {
    public KickCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 0) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
        } else {
            Player other = PlayerUtils.getPlayerStat(args[0]);

            if (other == null) {
                PlayerUtils.addStat(new Player(UUIDFetcher.getCachedUUID(args[0])));
                other = PlayerUtils.getPlayerStat(args[0]);
                if (other == null) {
                    StreamLine.getInstance().getLogger().severe("CANNOT INSTANTIATE THE PLAYER: " + args[0]);
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeCommandErrorUnd);
                    return;
                }
            }

            if (other.uuid == null) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                return;
            }

            if (! other.online) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer);
                return;
            }

            String reason = TextUtils.argsToStringMinus(args, 0);

            ProxiedPlayer pp = UUIDFetcher.getPPlayerByUUID(other.uuid);

            if (pp != null) {
                pp.disconnect(TextUtils.codedText(MessageConfUtils.kickKicked
                        .replace("%reason%", reason)
                ));
            }

            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.kickSender
                    .replace("%reason%", reason)
                    .replace("%player%", PlayerUtils.getOffOnDisplayBungee(other))
            );
        }
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
        Collection<ProxiedPlayer> players = StreamLine.getInstance().getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();

        for (ProxiedPlayer player : players){
            if (sender instanceof ProxiedPlayer) if (player.equals(sender)) continue;
            strPlayers.add(player.getName());
        }

        if (args.length == 1) {
            return TextUtils.getCompletion(strPlayers, args[0]);
        }

        return new ArrayList<>();
    }
}
