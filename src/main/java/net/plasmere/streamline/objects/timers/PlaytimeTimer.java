package net.plasmere.streamline.objects.timers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.PlayerUtils;

import java.util.*;

public class PlaytimeTimer implements Runnable {
    public int countdown;
    public int reset;

    public PlaytimeTimer(int seconds) {
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

                p.addPlaySecond(1);
            }

            //if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("Just gave out PlayTime to " + StreamLine.getInstance().getProxy().getPlayers().size() + " online players!");
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
    }
}
