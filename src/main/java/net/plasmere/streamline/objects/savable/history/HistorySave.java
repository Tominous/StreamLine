package net.plasmere.streamline.objects.savable.history;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.utils.MessagingUtils;

import java.io.File;
import java.io.FileWriter;

public class HistorySave {
    public File saveFile;
    public String uuid;

    public HistorySave(String uuid) {
        this.saveFile = new File(StreamLine.getInstance().getChatHistoryDir(), uuid + ".save");
        this.uuid = uuid;
    }

    public String addLine(String line) {
        try {
            if (! saveFile.exists()) if (! saveFile.createNewFile()) if (ConfigUtils.debug) MessagingUtils.logWarning("Cannot create file for " + uuid);

            FileWriter writer = new FileWriter(saveFile);
            writer.write("\n" + line);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return line;
    }
}
