package net.plasmere.streamline.listeners;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.Config;
import net.plasmere.streamline.utils.ConfigUtils;
import net.plasmere.streamline.utils.MessageConfUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.TextUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class StaffChatListener implements Listener {
    private final Configuration config = Config.getConf();
    private final String prefix = ConfigUtils.moduleStaffChatPrefix;
    private final StreamLine plugin;

    public StaffChatListener(StreamLine streamLine){
        this.plugin = streamLine;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(ChatEvent e){
        if (e.isCancelled()) return;
        if (! (e.getSender() instanceof ProxiedPlayer)) return;

        ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
        String msg = e.getMessage();

        if (TextUtils.isCommand(msg)) return;

        if (ConfigUtils.moduleStaffChat) {
            if (ConfigUtils.moduleStaffChatDoPrefix) {
                if (msg.startsWith(prefix) && !prefix.equals("/")) {
                    if (!sender.hasPermission("streamline.staff")) {
                        e.setCancelled(true);
                        return;
                    }

                    if (msg.equals(prefix)) {
                        sender.sendMessage(TextUtils.codedText(MessageConfUtils.staffChatWrongPrefix.replace("%newline%", "\n")));
                        e.setCancelled(true);
                        return;
                    }

                    e.setCancelled(true);
                    MessagingUtils.sendStaffMessage(sender, MessageConfUtils.bungeeStaffChatFrom, msg.substring(prefix.length()), plugin);
                    MessagingUtils.sendDiscordStaffMessageSC(sender, msg.substring(prefix.length()));
                }
            }
        }
    }
}
