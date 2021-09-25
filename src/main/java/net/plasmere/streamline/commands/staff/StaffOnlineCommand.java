package net.plasmere.streamline.commands.staff;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.utils.PlayerUtils;

import java.util.*;

public class StaffOnlineCommand extends Command {

    public StaffOnlineCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    public void execute(CommandSender sender, String[] args){
        Collection<ProxiedPlayer> staffs = StreamLine.getInstance().getProxy().getPlayers();
        Set<ProxiedPlayer> lstaffs = new HashSet<>(staffs);

        for (ProxiedPlayer player : staffs){
            try {
                if (! player.hasPermission(ConfigUtils.staffPerm)) {
                    lstaffs.remove(player);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        MessagingUtils.sendBUserMessage(sender,
                MessageConfUtils.sOnlineBungeeMain()
                        .replace("%amount%", Integer.toString(lstaffs.size()))
                        .replace("%staffbulk%", getStaffList(lstaffs))
        );
    }

    private static String getStaffList(Set<ProxiedPlayer> lstaffs){
        StringBuilder staff = new StringBuilder();
        int i = 1;

        for (ProxiedPlayer player : lstaffs){
            if (i < lstaffs.size())
                staff.append(MessageConfUtils.sOnlineBungeeBulkNotLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreatePlayerStat(player)))
                        .replace("%server%", player.getServer().getInfo().getName().toLowerCase())
                );
            else
                staff.append(MessageConfUtils.sOnlineBungeeBulkLast()
                        .replace("%player_display%", PlayerUtils.getOffOnDisplayBungee(PlayerUtils.getOrCreatePlayerStat(player)))
                        .replace("%server%", player.getServer().getInfo().getName().toLowerCase())
                );
            i++;
        }

        return staff.toString();
    }
}
