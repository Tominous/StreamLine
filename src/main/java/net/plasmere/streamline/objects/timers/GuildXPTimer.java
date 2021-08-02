package net.plasmere.streamline.objects.timers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Guild;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.objects.users.SavableUser;
import net.plasmere.streamline.utils.GuildUtils;
import net.plasmere.streamline.utils.PlayerUtils;

import java.util.ArrayList;
import java.util.List;

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
            List<SavableUser> users = new ArrayList<>(PlayerUtils.getStats());

            for (SavableUser user : users) {
                if (user.guild == null) user.updateKey("guild", "");
                if (user.guild.equals("")) continue;

                Guild guild = GuildUtils.getOrGetGuild(user.guild);

                guild.addTotalXP(ConfigUtils.xpPerGiveG);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        //MessagingUtils.logInfo("Just gave " + ConfigUtils.xpPerGiveG + " GEXP to " + GuildUtils.getGuilds().size() + " guilds!");

        //StreamLine.getInstance().getProxy().getScheduler().schedule(StreamLine.getInstance(), new GuildXPTimer(ConfigUtils.timePerGiveG), 1, 1, TimeUnit.SECONDS);
    }
}
