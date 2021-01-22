package net.plasmere.streamline.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;

public class StatsCommand extends Command {
    private final StreamLine plugin;

    public StatsCommand(StreamLine streamLine, String perm, String[] aliases){
        super("stats", perm, aliases);
        this.plugin = streamLine;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            Player player = PlayerUtils.getStat(sender);
            if (args.length <= 0 || ! ConfigUtils.comBStatsOthers) {
                PlayerUtils.info(player, player);
            } else {
                if (player.hasPermission(ConfigUtils.comBStatsPermOthers)){
                    PlayerUtils.info(player, PlayerUtils.getStat(args[0]));
                } else {
                    MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPerm);
                }
            }
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.onlyPlayers);
        }
    }
}
