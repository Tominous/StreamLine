package net.plasmere.streamline.commands.staff;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;

import java.util.*;

public class SudoCommand extends Command implements TabExecutor {
    private final StreamLine plugin;

    public SudoCommand(StreamLine plugin, String perm, String[] aliases){
        super("bsudo", perm, aliases);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore);
        } else {
            ProxiedPlayer sudoOn = plugin.getProxy().getPlayer(args[0]);


            List<String> nargs = new ArrayList<>(Arrays.asList(args));

            nargs.remove(args[0]);

            if (sudoOn.hasPermission(ConfigUtils.noSudoPerm)){
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.sudoNoSudo
                        .replace("%user%", sudoOn.getDisplayName())
                );
                return;
            }

            if (plugin.getProxy().getPluginManager().dispatchCommand(sudoOn, TextUtils.normalize(nargs))){
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.sudoWorked
                        .replace("%user%", sudoOn.getDisplayName())
                );
            } else {
                MessagingUtils.sendBUserMessage(sender, MessageConfUtils.sudoNoWork
                        .replace("%user%", sudoOn.getDisplayName())
                );
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
        Collection<ProxiedPlayer> players = plugin.getProxy().getPlayers();
        List<String> strPlayers = new ArrayList<>();

        for (ProxiedPlayer player : players){
            strPlayers.add(Objects.requireNonNull(PlayerUtils.getStat(player)).getName());
        }

        Collection<Map.Entry<String, Command>> commands = plugin.getProxy().getPluginManager().getCommands();
        List<String> strCommands = new ArrayList<>();

        for (Map.Entry<String, Command> com : commands){
            strCommands.add(com.getValue().getName());
        }

        if (args.length == 1) {
            return strPlayers;
        } else if (args.length == 2){
            return strCommands;
        }

        return new ArrayList<>();
    }
}
