package net.plasmere.streamline.utils.holders;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.md_5.bungee.api.ProxyServer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.utils.MessagingUtils;

import java.util.UUID;

public class ViaHolder {
    public ViaAPI via;
    public boolean enabled;

    public ViaHolder(){
        enabled = isPresent();
    }

    public boolean isPresent(){
        if (ProxyServer.getInstance().getPluginManager().getPlugin("ViaVersion") == null) {
            return false;
        }

        try {
            via = Via.getAPI();
            return true;
        } catch (Exception e) {
            MessagingUtils.logSevere("ViaVersion not loaded... Disabling ViaVersion support...");
        }
        return false;
    }

    public ProtocolVersion getVersion(int version){
        return ProtocolVersion.getProtocol(version);
    }

    public ProtocolVersion getProtocal(UUID uuid){
        return ProtocolVersion.getProtocol(via.getPlayerVersion(uuid));
    }
}