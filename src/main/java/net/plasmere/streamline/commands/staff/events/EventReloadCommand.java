package net.plasmere.streamline.commands.staff.events;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.events.EventsHandler;
import net.plasmere.streamline.utils.MessagingUtils;

public class EventReloadCommand extends Command {
    public EventReloadCommand(String perm, String[] aliases){
        super("evreload", perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(ConfigUtils.comBEReloadPerm)) {
            EventsHandler.unloadEvents();
            StreamLine.getInstance().loadEvents();

            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.evReload
                    .replace("%count%", String.valueOf(EventsHandler.getEvents().size()))
            );
        } else {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPerm);
        }
    }
}
