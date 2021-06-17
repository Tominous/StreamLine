package net.plasmere.streamline.listeners;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.utils.TextUtils;

import java.util.UUID;

public class ProxyPingListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProxyPing(ProxyPingEvent event){
        ServerPing response = event.getResponse();

        if (response == null || (event instanceof Cancellable && ((Cancellable) event).isCancelled())) return;

        ServerPing.Players players = response.getPlayers();
        int onlinePlayers = players.getOnline();
        int maxPlayers = players.getMax();

        if (ConfigUtils.scOnlinePlayers) {
            onlinePlayers = StreamLine.serverConfig.onlinePlayers();

            players.setOnline(onlinePlayers);
        }

        if (ConfigUtils.scMaxPlayers) {
            maxPlayers = StreamLine.serverConfig.maxPlayers();

            players.setMax(maxPlayers);
        }

        if (ConfigUtils.scMOTD) {
            response.setDescriptionComponent(TextUtils.clhText(StreamLine.getInstance().getCurrentMOTD()
                        .replace("%online%", String.valueOf(onlinePlayers))
                        .replace("%max%", String.valueOf(maxPlayers))
                    , ConfigUtils.linkPre));

//            if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info(TextUtils.codedString(StreamLine.getInstance().getCurrentMOTD()
//                    .replace("%online%", String.valueOf(StreamLine.getInstance().getProxy().getPlayers().size()))
//                    .replace("%max%", String.valueOf(StreamLine.getInstance().getProxy().getConfig().getPlayerLimit()))));
        }

        if (ConfigUtils.scVersion) {
            response.getVersion().setName(StreamLine.serverConfig.getVersion());

//            if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info(StreamLine.serverConfig.getVersion());
        }

        if (ConfigUtils.scSample) {
            UUID fake = new UUID(0, 0);
            String[] sampleString = StreamLine.serverConfig.getSampleArray();
            ServerPing.PlayerInfo[] sample = new ServerPing.PlayerInfo[sampleString.length];

            for (int i = 0; i < sampleString.length; i++) {
                sample[i] = new ServerPing.PlayerInfo(sampleString[i]
                        .replace("%online%", String.valueOf(onlinePlayers))
                        .replace("%max%", String.valueOf(maxPlayers))
                        , fake);
            }

            players.setSample(sample);

//            if (ConfigUtils.debug) {
//                for (String s : sampleString) {
//                    StreamLine.getInstance().getLogger().info(s);
//                }
//            }
        }
    }
}
