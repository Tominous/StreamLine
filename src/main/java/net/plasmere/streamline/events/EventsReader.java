package net.plasmere.streamline.events;

import java.io.File;

public class EventsReader {
    public static Event fromFile(File file){
        try {
            return new Event(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
