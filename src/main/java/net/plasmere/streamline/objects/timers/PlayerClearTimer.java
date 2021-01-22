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
        try {
            for (ProxiedPlayer pl : StreamLine.getInstance().getProxy().getPlayers()){
                for (Player player : PlayerUtils.getStats()) {
                    if (pl.getName().equals(player.getName())) continue;

                    player.saveInfo();
                    PlayerUtils.removeStat(player);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        StreamLine.getInstance().getLogger().info("Just removed some cached players... Now at " + PlayerUtils.getStats().size() + " cached players!");
    }
}
