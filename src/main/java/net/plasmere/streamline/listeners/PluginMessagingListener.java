package net.plasmere.streamline.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.utils.MessagingUtils;

public class PluginMessagingListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPluginMessage(PluginMessageEvent event) {
        /*
        https://www.spigotmc.org/wiki/sending-a-custom-plugin-message-from-bungeecord/
         */

        if (! event.getTag().equalsIgnoreCase(StreamLine.customChannel)) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();
        if (subChannel.equalsIgnoreCase("send.displayname"))
        {
            // the receiver is a ProxiedPlayer when a server talks to the proxy
            if (event.getReceiver() instanceof ProxiedPlayer)
            {
                ProxiedPlayer receiver = (ProxiedPlayer) event.getReceiver();
                MessagingUtils.serveredUsernames.put(receiver, in.readUTF());
            }
        }
    }
}
