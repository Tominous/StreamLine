package net.plasmere.streamline.objects.timers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.GuildUtils;
import net.plasmere.streamline.utils.PlayerUtils;

public class PlayerXPTimer implements Runnable {
    public int countdown;
    public int reset;

    public PlayerXPTimer(int seconds) {
        this.countdown = seconds;
        this.reset = seconds;
    }

    @Override
    public void run() {
        if (countdown == 0) {
            done();
        }

        countdown--;
    }

    public void done(){
        countdown = reset;
        try {
            for (ProxiedPlayer player : StreamLine.getInstance().getProxy().getPlayers()) {
                if (PlayerUtils.getStat(player) == null) continue;

                Player p = PlayerUtils.getStat(player);
                assert p != null;
                p.addXp(ConfigUtils.xpPerGive);

                p.saveInfo();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        StreamLine.getInstance().getLogger().info("Just gave " + ConfigUtils.xpPerGive + " GEXP to " + GuildUtils.getGuilds().size() + " guilds!");
    }
}
