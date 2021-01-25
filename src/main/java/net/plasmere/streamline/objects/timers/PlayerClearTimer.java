package net.plasmere.streamline.objects.timers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.GuildUtils;
import net.plasmere.streamline.utils.PlayerUtils;

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

        int count = 0;

        try {
            for (Player player : PlayerUtils.getStats()) {
                if (! player.online) {
                    player.saveInfo();
                    PlayerUtils.removeStat(player);

                    count++;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        StreamLine.getInstance().getLogger().info("Just removed " + count + " cached players... Now at " + PlayerUtils.getStats().size() + " cached players!");
    }
}
