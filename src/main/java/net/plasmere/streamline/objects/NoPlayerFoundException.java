package net.plasmere.streamline.objects;

public class NoPlayerFoundException extends Exception {
    public NoPlayerFoundException(String playerName){
        super("Could not find player --> " + playerName + " :");
    }
}
