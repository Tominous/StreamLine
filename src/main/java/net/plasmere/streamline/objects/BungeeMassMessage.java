package net.plasmere.streamline.objects;

import net.md_5.bungee.api.CommandSender;

public class BungeeMassMessage {
    public CommandSender sender;
    public String title;
    public String transition;
    public String message;
    public String permission;

    public BungeeMassMessage(CommandSender sender, String title, String transition, String message, String permission) {
        this.sender = sender;
        this.title = title + " ";
        this.transition = transition + " ";
        this.message = message;
        this.permission = permission;
    }

    public BungeeMassMessage(CommandSender sender, String message, String permission) {
        this.sender = sender;
        this.title = "";
        this.transition = "";
        this.message = message;
        this.permission = permission;
    }
}
