package net.plasmere.streamline.objects.timers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.PlayerUtils;

import java.util.ConcurrentModificationException;

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

                p.saveInfo();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            for (Player player : PlayerUtils.getStats()) {
                if (!player.online) {
                    player.saveInfo();
                    PlayerUtils.removeStat(player);
                }
            }
        } catch (ConcurrentModificationException e) {
            // do nothing
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
