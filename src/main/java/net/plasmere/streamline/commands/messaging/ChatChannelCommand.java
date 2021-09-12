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
                    player.setChatLevel(ChatLevel.LOCAL);

                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.chatChannelsLocalSwitch().replace());
                } else if (allowGlobal && ! allowLocal) {
                    player.setChatLevel(ChatLevel.GLOBAL);
                }
            }


        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers());
        }
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
