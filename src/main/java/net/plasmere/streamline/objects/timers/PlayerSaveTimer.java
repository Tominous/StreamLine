package net.plasmere.streamline.objects.timers;

import net.plasmere.streamline.utils.PlayerUtils;

public class PlayerSaveTimer implements Runnable {
    public int countdown;
    public int reset;

    public PlayerSaveTimer(int seconds) {
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

        int count = PlayerUtils.saveAll();

        //MessagingUtils.logInfo("Just removed " + count + " cached players... Now at " + PlayerUtils.getJustPlayers().size() + " cached players!");
    }
}
