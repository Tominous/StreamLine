package net.plasmere.streamline.utils.holders;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.objects.GeyserFile;
import net.plasmere.streamline.utils.MessagingUtils;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;

import java.io.File;
import java.util.UUID;

public class GeyserHolder {
    public File playerPath = new File(StreamLine.getInstance().getPlDir(), "geyser" + File.separator);
    public GeyserConnector connector;
    public boolean enabled;
    public GeyserFile file;

    public GeyserHolder(){
        enabled = isPresent();

        if (enabled) {
            setUpPath();
            file = new GeyserFile(false);
        }
    }

    public boolean isPresent(){
        if (ProxyServer.getInstance().getPluginManager().getPlugin("Geyser-BungeeCord") == null) {
            return false;
        }

        try {
            this.connector = GeyserConnector.getInstance();
            MessagingUtils.logInfo("Geyser is installed... Using Geyser support...");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setUpPath(){
        if (! playerPath.exists()) {
            if (! playerPath.mkdirs()) {
                MessagingUtils.logSevere("Error setting up the Geyser player path...");
            }
        }
    }

    public void checkConnector(){
        if (connector == null) this.connector = GeyserConnector.getInstance();
    }

    public boolean isGeyserPlayer(ProxiedPlayer player) {
        checkConnector();

        for (GeyserSession session : connector.getPlayers()) {
            if (session.getName().equals(player.getName())) return true;
        }

        return false;
    }

    public boolean isGeyserPlayer(String player) {
        checkConnector();

        for (GeyserSession session : connector.getPlayers()) {
            if (session.getName().equals(player)) return true;
        }

        return false;
    }

    public String getGeyserUUID(String player) {
        checkConnector();

        for (GeyserSession session : connector.getPlayers()) {
            if (session.getName().equals(player)) {
                return session.getAuthData().getXboxUUID();
            }
        }

        return null;
    }

    public ProxiedPlayer getPPlayerByUUID(String uuid){
        checkConnector();

        for (GeyserSession session : connector.getPlayers()) {
            if (session.getAuthData().getXboxUUID().equals(uuid)) StreamLine.getInstance().getProxy().getPlayer(session.getName());
        }

        return null;
    }
}
