package net.plasmere.streamline.objects.timers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.users.Player;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;

import java.util.ArrayList;
import java.util.List;

public class PlayerXPTimer implements Runnable {
    public int countdown;
    public int reset;

    public PlayerXPTimer(int seconds) {
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
                player.addTotalXP(ConfigUtils.xpPerGiveP);
            }

//            if (ConfigUtils.debug) MessagingUtils.logInfo("Just gave out XP to " + PlayerUtils.getJustPlayersOnline().size() + " online players!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
