package net.plasmere.streamline.commands;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StreamCommand extends Command {
    private String perm = "";

    public StreamCommand(String base, String perm, String[] aliases) {
        super(base, perm, aliases);

        this.perm = perm;
    }

    @Override
    public void execute(CommandSender sender, String[] args){
        if (sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (player.hasPermission(perm)){
                if (args.length != 1){
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.streamNeedLink());
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeImproperUsage()
                            .replace("%usage%", "/stream <link>")
                    );
                } else {
                    if (! args[0].startsWith("https://") && ! args[0].startsWith("http://") && ! args[0].startsWith("ftp://") && ! args[0].startsWith("sftp://")) {
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.streamNotLink());
                        MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeImproperUsage()
                                .replace("%usage%", "/stream <link>")
                        );
                    } else {
                        MessagingUtils.sendBCLHBroadcast(sender, MessageConfUtils.streamMessage()
                                .replace("%user_display%", sender.getName())
                                .replace("%link%", args[0])
                                , MessageConfUtils.streamHoverPrefix()
                        );
                    }
                }
            } else {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPerm());
            }
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers());
        }
    }
}
