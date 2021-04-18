package net.plasmere.streamline.objects.timers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.utils.PlayerUtils;

import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

public class PlayerXPTimer implements Runnable {
    public int countdown;
    public int reset;

    public PlayerXPTimer(int seconds) {
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

                p.addXp(ConfigUtils.xpPerGiveP);

                p.saveInfo();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            ListIterator<Player> players = PlayerUtils.getStats().listIterator();

            while (players.hasNext()) {
                Player player = players.next();

                if (! player.online) {
                    player.saveInfo();
                    players.remove();

                    PlayerUtils.removeStat(player);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        //StreamLine.getInstance().getLogger().info("Just gave " + ConfigUtils.xpPerGiveP + " Network EXP to " + PlayerUtils.getStats().size() + " players!");

        //StreamLine.getInstance().getProxy().getScheduler().schedule(StreamLine.getInstance(), new PlayerXPTimer(ConfigUtils.timePerGiveP), 1, 1, TimeUnit.SECONDS);
    }
}
