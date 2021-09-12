package net.plasmere.streamline.objects.timers;

import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.savable.users.Player;
import net.plasmere.streamline.utils.PlayerUtils;

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
