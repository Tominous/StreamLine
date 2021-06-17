package net.plasmere.streamline.objects.timers;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.objects.lists.SingleSet;
import net.plasmere.streamline.utils.PlayerUtils;

import java.util.*;

public class OneSecondTimer implements Runnable {
    public int countdown;
    public int reset;

    public OneSecondTimer() {
        this.countdown = 1;
        this.reset = 1;
    }

    @Override
    public void run() {
        if (countdown == 0) {
            done();
        }

        countdown--;
    }

    public void done(){
        try {
            countdown = reset;

            Set<Player> c = PlayerUtils.getConnections().keySet();
            List<Player> conns = new ArrayList<>(c);
            List<Player> toRemove = new ArrayList<>();

            for (Player player : conns) {
                SingleSet<Integer, Integer> conn = PlayerUtils.getConnection(player);

                if (conn == null) continue;

                PlayerUtils.removeSecondFromConn(player, conn);

                conn = PlayerUtils.getConnection(player);

                if (conn == null) continue;
                if (conn.key <= 0) toRemove.add(player);
            }

            for (Player remove : toRemove) {
                PlayerUtils.removeConn(remove);
            }

            if (StreamLine.lpHolder.enabled) {
                for (Player player : PlayerUtils.getStats()) {
                    if (player.latestName == null) continue;
                    if (player.latestName.equals("")) continue;
                    PlayerUtils.updateDisplayName(player);
                }
            }

            for (Player player : PlayerUtils.getStats()) {
                PlayerUtils.checkAndUpdateIfMuted(player);
            }

            PlayerUtils.saveAll();
        } catch (ConcurrentModificationException e) {
            if (ConfigUtils.debug) e.printStackTrace();
        }
    }
}
