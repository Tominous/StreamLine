package net.plasmere.streamline.objects;

import net.md_5.bungee.api.CommandSender;

public class BungeeMessage {
    public CommandSender sender;
    public CommandSender to;
    public String title;
    public String transition;
    public String message;

    public BungeeMessage(CommandSender sender, CommandSender to, String title, String transition, String message){
        this.sender = sender;
        this.to = to;
        this.title = title + " ";
        this.transition = transition + " ";
        this.message = message;
    }

    public BungeeMessage(CommandSender sender, CommandSender to, String message){
        this.sender = sender;
        this.to = to;
        this.title = "";
        this.transition = "";
        this.message = message;
    }
}
