package net.plasmere.streamline.commands.staff.punishments;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.DiscordBotConfUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.messaging.DiscordMessage;
import net.plasmere.streamline.objects.savable.users.Player;
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
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
        } else {
            Player other = PlayerUtils.getOrGetPlayerStat(args[0]);

            if (other == null) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer());
                return;
            }

            if (! other.online) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer());
                return;
            }

            if (PlayerUtils.hasOfflinePermission(ConfigUtils.punKicksBypass, other.uuid)) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.kickCannot());
                return;
            }

            String reason = TextUtils.argsToStringMinus(args, 0);

            PlayerUtils.kick(other, MessageConfUtils.kickKicked()
                    .replace("%reason%", reason)
            );

            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.kickSender()
                    .replace("%reason%", reason)
                    .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(other))
                    .replace("%reason%", reason)
            );

            if (ConfigUtils.moduleDEnabled) {
                if (ConfigUtils.punKicksDiscord) {
                    MessagingUtils.sendDiscordEBMessage(
                            new DiscordMessage(
                                    sender,
                                    MessageConfUtils.kickEmbed(),
                                    MessageConfUtils.kickDiscord()
                                            .replace("%punisher%", sender.getName())
                                            .replace("%player_display%", other.latestName)
                                            .replace("%reason%", reason)
                                    ,
                                    DiscordBotConfUtils.textChannelKicks
                            )
                    );
                }
            }

            MessagingUtils.sendPermissionedMessageNonSelf(sender, ConfigUtils.staffPerm, MessageConfUtils.kickStaff()
                    .replace("%punisher%", sender.getName())
                    .replace("%player_display%", other.latestName)
                    .replace("%reason%", reason)
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
