package net.plasmere.streamline.objects.timers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.utils.GuildUtils;
import net.plasmere.streamline.utils.PlayerUtils;

public class GuildXPTimer implements Runnable {
    public int countdown;
    public int reset;

    public GuildXPTimer(int seconds) {
        this.countdown = 0;
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
            for (ProxiedPlayer pl : StreamLine.getInstance().getProxy().getPlayers()){
                Player player = PlayerUtils.getPlayerStat(pl);
                if (GuildUtils.getGuild(player) == null) continue;

                Guild guild = GuildUtils.getGuild(player);

                if (guild == null) continue;

                guild.addTotalXP(ConfigUtils.xpPerGiveG);

                guild.saveInfo();

                //MessagingUtils.sendBGUserMessage(guild, StreamLine.getInstance().getProxy().getConsole(), pl, "&eJust gave you &6" + ConfigUtils.xpPerGiveG + " &2GEXP&e!");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        //StreamLine.getInstance().getLogger().info("Just gave " + ConfigUtils.xpPerGiveG + " GEXP to " + GuildUtils.getGuilds().size() + " guilds!");

        //StreamLine.getInstance().getProxy().getScheduler().schedule(StreamLine.getInstance(), new GuildXPTimer(ConfigUtils.timePerGiveG), 1, 1, TimeUnit.SECONDS);
    }
}
