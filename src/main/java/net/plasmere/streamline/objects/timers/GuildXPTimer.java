package net.plasmere.streamline.objects.timers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.utils.GuildUtils;

public class GuildXPTimer implements Runnable {
    public int countdown;
    public int reset;

    public GuildXPTimer(int seconds) {
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
                if (! GuildUtils.getGuilds().contains(GuildUtils.getGuild(player))) continue;

                Guild guild = GuildUtils.getGuild(player);
                guild.addXp(ConfigUtils.xpPerGive);

                guild.saveInfo();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        StreamLine.getInstance().getLogger().info("Just gave " + ConfigUtils.xpPerGive + " GEXP to " + GuildUtils.getGuilds().size() + " guilds!");
    }
}
