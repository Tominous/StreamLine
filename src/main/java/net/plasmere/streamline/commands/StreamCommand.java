package net.plasmere.streamline.commands;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.utils.ConfigUtils;
import net.plasmere.streamline.utils.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;

public class StreamCommand extends Command {
    private final StreamLine plugin;
    private String perm = "";

    public StreamCommand(StreamLine streamLine, String perm, String[] aliases) {
        super("stream", perm, aliases);

        this.perm = perm;
        this.plugin = streamLine;
    }

    @Override
    public void execute(CommandSender sender, String[] args){
        if (sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (player.hasPermission(perm)){
                if (args.length != 1){
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.streamNeedLink);
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeImproperUsage
                            .replace("%usage%", "/stream <link>")
                    );
                } else {
                    if (! args[0].startsWith("https://")){
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.streamNotLink);
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeImproperUsage
                                .replace("%usage%", "/stream <link>")
                        );
                    } else {
                        MessagingUtils.sendBCLHBroadcast(sender, MessageConfUtils.streamMessage
                                .replace("%user%", sender.getName())
                                .replace("%link%", args[0])
                                , MessageConfUtils.streamHoverPrefix
                        );
                    }
                }
            } else {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPerm);
            }
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers);
        }
    }
}
