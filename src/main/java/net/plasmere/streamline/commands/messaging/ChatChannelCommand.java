package net.plasmere.streamline.commands.messaging;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.savable.users.ChatLevel;
import net.plasmere.streamline.objects.savable.users.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.PluginUtils;
import net.plasmere.streamline.utils.TextUtils;

import java.util.ArrayList;
import java.util.TreeSet;

public class ChatChannelCommand extends Command implements TabExecutor {

    public ChatChannelCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            Player player = PlayerUtils.getPlayerStat(sender);
            if (player == null) return;

            if (args.length <= 0) {
                boolean allowGlobal = StreamLine.serverConfig.getAllowGlobal();
                boolean allowLocal = StreamLine.serverConfig.getAllowLocal();

                if (allowGlobal && allowLocal) {
                    ChatLevel newLevel = ChatLevel.LOCAL;

                    sendMessageFormatted(sender, MessageConfUtils.chatChannelsLocalSwitch(), newLevel, player.chatLevel);

                    player.setChatLevel(newLevel);
                } else if (allowGlobal && ! allowLocal) {
                    ChatLevel newLevel = ChatLevel.GLOBAL;

                    sendMessageFormatted(sender, MessageConfUtils.chatChannelsGlobalSwitch(), newLevel, player.chatLevel);

                    player.setChatLevel(newLevel);
                } else if (! allowGlobal && ! allowLocal) {
                    ChatLevel newLevel = ChatLevel.LOCAL;

                    sendMessageFormatted(sender, MessageConfUtils.chatChannelsLocalSwitch(), newLevel, player.chatLevel);

                    player.setChatLevel(newLevel);
                }

                return;
            }

            if (args.length > 1) {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsLess());
                return;
            }

            ChatLevel newLevel = Player.parseChatLevel(args[0]);

            switch (newLevel) {
                case LOCAL:
                    sendMessageFormatted(sender, MessageConfUtils.chatChannelsLocalSwitch(), newLevel, player.chatLevel);
                    break;
                case GLOBAL:
                    sendMessageFormatted(sender, MessageConfUtils.chatChannelsGlobalSwitch(), newLevel, player.chatLevel);
                    break;
                case GUILD:
                    sendMessageFormatted(sender, MessageConfUtils.chatChannelsGuildSwitch(), newLevel, player.chatLevel);
                    break;
                case PARTY:
                    sendMessageFormatted(sender, MessageConfUtils.chatChannelsPartySwitch(), newLevel, player.chatLevel);
                    break;
                case GOFFICER:
                    sendMessageFormatted(sender, MessageConfUtils.chatChannelsGOfficerSwitch(), newLevel, player.chatLevel);
                    break;
                case POFFICER:
                    sendMessageFormatted(sender, MessageConfUtils.chatChannelsPOfficerSwitch(), newLevel, player.chatLevel);
                    break;
            }

            player.setChatLevel(newLevel);
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers());
        }
    }

    public void sendMessageFormatted(CommandSender sender, String formatFrom, ChatLevel newLevel, ChatLevel oldLevel) {
        MessagingUtils.sendBUserMessage(sender, formatFrom
                .replace("%new_channel%", newLevel.toString())
                .replace("%old_channel%", oldLevel.toString())
        );
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        TreeSet<String> options = new TreeSet<>();

        options.add("local");
        options.add("global");
        options.add("guild");
        options.add("party");
        options.add("g-officer");
        options.add("p-officer");

        if (args.length <= 1) {
            return TextUtils.getCompletion(options, args[0]);
        } else {
            return new ArrayList<>();
        }
    }
}
