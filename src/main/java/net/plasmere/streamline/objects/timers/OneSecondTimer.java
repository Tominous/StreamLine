package net.plasmere.streamline.objects.timers;

import net.plasmere.streamline.objects.Player;
import net.plasmere.streamline.objects.lists.SingleSet;
import net.plasmere.streamline.utils.PlayerUtils;

import java.util.HashMap;
import java.util.Iterator;

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
        countdown = reset;

        Iterator<Player> conns = PlayerUtils.getConnections().keySet().iterator();

        while (conns.hasNext()) {
            Player player = conns.next();

            PlayerUtils.removeSecondFromConn(player);

            SingleSet<Integer, Integer> conn = PlayerUtils.getConnection(player);
            if (conn == null) continue;
            if (conn.key <= 0) PlayerUtils.removeConn(player);
        }
    }
}
