package net.plasmere.streamline.objects.timers;

import net.plasmere.streamline.objects.savable.users.Player;
import net.plasmere.streamline.utils.PlayerUtils;

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
