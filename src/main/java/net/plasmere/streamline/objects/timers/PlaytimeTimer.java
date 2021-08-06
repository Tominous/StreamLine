package net.plasmere.streamline.objects.timers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.utils.PlayerUtils;

import java.util.*;

public class PlaytimeTimer implements Runnable {
    public int countdown;
    public int reset;

    public PlaytimeTimer(int seconds) {
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
            for (Player player : PlayerUtils.getJustPlayersOnline()) {
                player.addPlaySecond(1);
            }

            //if (ConfigUtils.debug) MessagingUtils.logInfo("Just gave out PlayTime to " + StreamLine.getInstance().getProxy().getPlayers().size() + " online players!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
