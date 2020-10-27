package net.plasmere.streamline.commands.staff;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;

public class StaffOnlineCommand extends Command {
    private final StreamLine plugin;

    public StaffOnlineCommand(StreamLine streamLine, String perm, String[] aliases){
        super("staffonline", perm, aliases);

        this.plugin = streamLine;
    }

    public void execute(CommandSender sender, String[] args){
        Collection<ProxiedPlayer> staffs = plugin.getProxy().getPlayers();
        Set<ProxiedPlayer> lstaffs = new HashSet<>(staffs);

        for (ProxiedPlayer player : staffs){
            try {
                if (! player.hasPermission("streamline.staff")) {
                    lstaffs.remove(player);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        MessagingUtils.sendBUserMessage(sender,
                MessageConfUtils.sOnlineBungeeMain
                        .replace("%amount%", Integer.toString(lstaffs.size()))
                        .replace("%staffbulk%", getStaffList(lstaffs))
        );
    }

    private static String getStaffList(Set<ProxiedPlayer> lstaffs){
        StringBuilder staff = new StringBuilder();
        int i = 1;

        for (ProxiedPlayer player : lstaffs){
            if (i < lstaffs.size())
                staff.append(MessageConfUtils.sOnlineBungeeBulkNotLast
                        .replace("%player%", player.getName())
                        .replace("%server%", player.getServer().getInfo().getName().toLowerCase())
                );
            else
                staff.append(MessageConfUtils.sOnlineBungeeBulkLast
                        .replace("%player%", player.getName())
                        .replace("%server%", player.getServer().getInfo().getName().toLowerCase())
                );
            i++;
        }

        return staff.toString();
    }
}
