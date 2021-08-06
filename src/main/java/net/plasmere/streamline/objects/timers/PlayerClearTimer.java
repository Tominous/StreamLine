package net.plasmere.streamline.objects.timers;

import net.plasmere.streamline.utils.GuildUtils;
import net.plasmere.streamline.utils.PlayerUtils;

public class PlayerClearTimer implements Runnable {
    public int countdown;
    public int reset;

    public PlayerClearTimer(int seconds) {
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

        int count = PlayerUtils.removeOfflineStats();

        //MessagingUtils.logInfo("Just removed " + count + " cached players... Now at " + PlayerUtils.getJustPlayers().size() + " cached players!");
    }
}
