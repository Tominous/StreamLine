package net.plasmere.streamline.utils.holders;

import net.md_5.bungee.api.ProxyServer;
import net.plasmere.streamline.StreamLine;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;

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
            StreamLine.getInstance().getLogger().severe("ViaVersion not loaded... Disabling ViaVersion support...");
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