package net.plasmere.streamline.objects.timers;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.objects.configs.ServerConfig;
import net.plasmere.streamline.utils.PlayerUtils;

public class MOTDUpdaterTimer implements Runnable {
    public int countdown;
    public int reset;

    public MOTDUpdaterTimer(int seconds) {
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

        Integer next = StreamLine.serverConfig.getComparedMOTD().higherKey(StreamLine.getInstance().getMotdPage());
        if (next == null){
            //if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("next is null...");
            next = StreamLine.serverConfig.getComparedMOTD().firstKey();
        }

        StreamLine.getInstance().setCurrentMOTD(StreamLine.serverConfig.getMOTDat(next));
        StreamLine.getInstance().setMotdPage(next);

        //if (ConfigUtils.debug) StreamLine.getInstance().getLogger().info("MOTD Updated!");
    }
}
