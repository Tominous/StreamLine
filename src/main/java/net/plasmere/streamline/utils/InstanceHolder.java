package net.plasmere.streamline.utils;

import net.plasmere.streamline.StreamLine;

public class InstanceHolder {
    public static StreamLine inst;

    public static void setInst(StreamLine streamLine){
        inst = streamLine;
    }

    public static StreamLine getInst(){
        return inst;
    }
}
