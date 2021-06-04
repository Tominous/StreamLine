package net.plasmere.streamline.objects.timers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.PlayerUtils;

import java.util.ArrayList;
import java.util.List;

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
                Player p = PlayerUtils.getStat(player);

                if (p == null) continue;
                if (! p.online) continue;

                p.addTotalXP(ConfigUtils.xpPerGiveP);

            }

            if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("Just gave out XP to " + StreamLine.getInstance().getProxy().getPlayers().size() + " online players!");
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            int count = 0;
            List<Player> players = PlayerUtils.getStats();
            List<Player> toRemove = new ArrayList<>();

            for (Player player : players) {
                if (! player.online) {
                    toRemove.add(player);
                }
            }

            for (Player player : toRemove) {
                try {
                    player.saveInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PlayerUtils.removeStat(player);
                count ++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        //StreamLine.getInstance().getLogger().info("Just gave " + ConfigUtils.xpPerGiveP + " Network EXP to " + PlayerUtils.getStats().size() + " players!");

        //StreamLine.getInstance().getProxy().getScheduler().schedule(StreamLine.getInstance(), new PlayerXPTimer(ConfigUtils.timePerGiveP), 1, 1, TimeUnit.SECONDS);
    }
}
