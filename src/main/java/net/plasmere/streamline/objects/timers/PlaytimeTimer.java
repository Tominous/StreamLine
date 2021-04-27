package net.plasmere.streamline.objects.timers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.PlayerUtils;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
            PlayerUtils.removePlayerIf(player -> {
                if (! player.onlineCheck()) {
                    try {
                        player.saveInfo();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    return true;
                }

                return false;
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
