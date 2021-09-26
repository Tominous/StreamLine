package net.plasmere.streamline.objects.savable.history;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.utils.MessagingUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.TreeMap;

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

            TreeMap<Integer, String> lines = new TreeMap<>();
            int l = 0;

            FileReader fileReader = new FileReader(saveFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String string = "";
            while ((string = bufferedReader.readLine()) != null) {
                lines.put(l, string);
                l ++;
            }
            bufferedReader.close();

            saveFile.delete();
            saveFile.createNewFile();

            FileWriter writer = new FileWriter(saveFile);
            for (int i = 0; i < lines.size(); i ++) {
                writer.write(lines.get(i) + "\n");
            }
            writer.write(line + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return line;
    }
}
