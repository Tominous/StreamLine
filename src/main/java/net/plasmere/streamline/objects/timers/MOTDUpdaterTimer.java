package net.plasmere.streamline.objects.timers;

import net.plasmere.streamline.StreamLine;

public class MOTDUpdaterTimer implements Runnable {
    public int countdown;
    public int reset;

    public MOTDUpdaterTimer(int seconds) {
        this.countdown = 0;
        this.reset = seconds;
    }

    @Override
    public void run() {
        if (countdown == reset && countdown <= -1) {
            return;
        }

        if (countdown == 0) {
            done();
        }

        countdown--;
    }

    public void done(){
        countdown = reset;

        Integer next = StreamLine.serverConfig.getComparedMOTD().higherKey(StreamLine.getInstance().getMotdPage());
        if (next == null){
            //if (ConfigUtils.debug) MessagingUtils.logInfo("next is null...");
            next = StreamLine.serverConfig.getComparedMOTD().firstKey();
        }

        StreamLine.getInstance().setCurrentMOTD(StreamLine.serverConfig.getMOTDat(next));
        StreamLine.getInstance().setMotdPage(next);

        //if (ConfigUtils.debug) MessagingUtils.logInfo("MOTD Updated!");
    }
}
