package net.plasmere.streamline.config.from;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.PluginUtils;

import java.io.FileWriter;

public class FindFrom {
    public static void doUpdate(String previousVersion, String language){

        if (PluginUtils.isFreshInstall()) {
            MessagingUtils.logInfo("Smells new in here!");
            MessagingUtils.logInfo("Please report any issues at https://discord.gg/tny494zXfn :)");

            return;
        }

        // TODO: MAKE SURE TO APPLY ALL PATCHES TO THE FIRST AND UP! (13.3 SHOULD HAVE ALL PATCHES APPLIED!)

        switch (previousVersion) {
            case "13.3":
                new From_1_0_13_3(language);
                new From_1_0_14_0(language);
                break;
            case "1.0.14.0":
                new From_1_0_14_0(language);
                break;
        }

        try {
            if (! StreamLine.getInstance().versionFile.delete()) if (ConfigUtils.debug) {
                MessagingUtils.logSevere("COULD NOT DELETE VERSION FILE!");
            }

            if (! StreamLine.getInstance().versionFile.createNewFile()) if (ConfigUtils.debug) {
                MessagingUtils.logSevere("COULD NOT CREATE VERSION FILE!");
            }

            FileWriter writer = new FileWriter(StreamLine.getInstance().versionFile);
            writer.write(StreamLine.getInstance().getDescription().getVersion());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
