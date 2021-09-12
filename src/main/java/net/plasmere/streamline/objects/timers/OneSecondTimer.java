package net.plasmere.streamline.objects.timers;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.savable.users.Player;
import net.plasmere.streamline.objects.savable.users.SavableUser;
import net.plasmere.streamline.utils.PlayerUtils;

import java.util.*;

public class OneSecondTimer implements Runnable {
    public int countdown;
    public int reset;

    public OneSecondTimer() {
        this.countdown = 0;
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

            if (PlayerUtils.getToSave().size() > 0) {
                for (SavableUser user : new ArrayList<>(PlayerUtils.getToSave())) {
                    PlayerUtils.doSave(user);
                }
            }

            PlayerUtils.tickConn();

            if (StreamLine.lpHolder.enabled) {
                for (Player player : PlayerUtils.getJustPlayersOnline()) {
                    if (player.latestName == null) continue;
                    if (player.latestName.equals("")) continue;
                    PlayerUtils.updateDisplayName(player);
                }
            }

            for (Player player : PlayerUtils.getJustPlayers()) {
                PlayerUtils.checkAndUpdateIfMuted(player);
            }

            PlayerUtils.tickTeleport();
        } catch (ConcurrentModificationException e) {
            if (ConfigUtils.debug) e.printStackTrace();
        }
    }
}
