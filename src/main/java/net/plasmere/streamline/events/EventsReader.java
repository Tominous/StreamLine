package net.plasmere.streamline.events;

import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class EventsReader {
    public static Event fromFile(File file){
        try {
            if (file.exists()) {
                Scanner reader = new Scanner(file);

                int linesAmount = (int) Files.lines(file.toPath()).count();
                String[] lines = new String[linesAmount];

                int i = 0;
                while (reader.hasNextLine()) {
                    lines[i] = reader.nextLine();
                    i ++;
                }

                return fromLines(lines);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Event fromLines(String[] lines) {
        try {
            List<String> tagList = new ArrayList<>();

            for (int i = 0; i < lines.length; i++) {
                if (! (lines[i].contains("value") || lines[i].contains("type"))) {
                    lines[i] = lines[i].replace(" ", "");
                    continue;
                }
                lines[i] = removeFirstSpaces(lines[i]);
                lines[i] = removeAfterColon(lines[i]);
            }

            //StreamLine.getInstance().getLogger().info(concatLines(lines));

            if (!lines[0].contains("tags")) throw new Exception("Invalid Event!");

            String[] splitTag = lines[0].split(":", 2);
            String[] tags = splitTag[1].split(",");

            tagList.addAll(Arrays.asList(tags));

            //StreamLine.getInstance().getLogger().info("EVR : tags -> " + tagList.toString());

            if (! (lines[2].contains("type") || lines[3].contains("value"))) throw new Exception("Invalid Event!");

            String[] splitCType = lines[2].split(":", 2);
            Event.Condition condition = Event.stringToCondition(splitCType[1]);

            //StreamLine.getInstance().getLogger().info("EVR : condition -> " + (condition != null ? Event.conditionToString(condition): "null"));

            if (! (lines[5].contains("type") || lines[6].contains("value"))) throw new Exception("Invalid Event!");

            String[] splitAType = lines[5].split(":", 2);
            Event.Action action = Event.stringToAction(splitAType[1]);

            //StreamLine.getInstance().getLogger().info("EVR : action -> " + (action != null ? Event.actionToString(action): "null"));

            return new Event(tagList, condition, lines[3].split(":")[1], action, lines[6].split(":")[1]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String concatLines(String[] lines){
        StringBuilder stringBuilder = new StringBuilder();

        int i = 1;
        for (String line : lines) {
            if (i < lines.length) {
                stringBuilder.append(line).append("\n");
            } else {
                stringBuilder.append(line);
            }
            i++;
        }

        return stringBuilder.toString();
    }

    public static String removeFirstSpaces(String line){
        char[] charLines = line.toCharArray();

        int spacesToRemove = 0;

        for (int i = 0; i < charLines.length; i++){
            if (charLines[i] != ' ') break;
            spacesToRemove++;
        }

        return line.substring(spacesToRemove);
    }

    public static String removeAfterColon(String line){
        try {
            String s1 = line.substring(0, line.indexOf(":") + 1);
            String s2 = line.substring(line.indexOf(":") + 2);
            // ":: "

            return s1 + s2;
        } catch (Exception e) {
            StreamLine.getInstance().getLogger().info("An event file wasn't set up correctly...");
            return "null";
        }
    }
}
