package net.plasmere.streamline.utils.holders;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ProxyServer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.utils.MessagingUtils;

public class LPHolder {
    public LuckPerms api;
    public boolean enabled;

    public LPHolder(){
        enabled = isPresent();
    }

    public boolean isPresent(){
        if (ProxyServer.getInstance().getPluginManager().getPlugin("ViaVersion") == null) {
            return false;
        }

        try {
            api = LuckPermsProvider.get();
            return true;
        } catch (Exception e) {
            MessagingUtils.logSevere("LuckPerms not loaded... Disabling LuckPerms support...");
        }
        return false;
    }
}
