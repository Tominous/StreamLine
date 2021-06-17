package net.plasmere.streamline.objects.timers;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.objects.configs.ServerConfig;
import net.plasmere.streamline.utils.PlayerUtils;

public class MOTDUpdaterTimer implements Runnable {
    public int countdown;
    public int reset;

    public MOTDUpdaterTimer(int ticks) {
        this.countdown = ticks;
        this.reset = ticks;
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

        Integer next = StreamLine.serverConfig.getComparedMOTD().higherKey(StreamLine.getInstance().getMotdPage());
        if (next == null) next = StreamLine.serverConfig.getComparedMOTD().firstKey();

        StreamLine.getInstance().setCurrentMOTD(StreamLine.serverConfig.getMOTDat(next));
        StreamLine.getInstance().setMotdPage(next);
    }
}
