package net.plasmere.streamline.objects.timers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.GuildUtils;
import net.plasmere.streamline.utils.PlayerUtils;

import java.util.ArrayList;
import java.util.List;

public class PlayerClearTimer implements Runnable {
    public int countdown;
    public int reset;

    public PlayerClearTimer(int seconds) {
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

        //StreamLine.getInstance().getLogger().info("Just removed " + count + " cached players... Now at " + PlayerUtils.getStats().size() + " cached players!");
    }
}
