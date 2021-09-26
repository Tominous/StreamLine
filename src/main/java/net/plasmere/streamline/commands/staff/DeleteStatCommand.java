package net.plasmere.streamline.commands.staff;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.plasmere.streamline.StreamLine;
import net.plasmere.streamline.config.ConfigUtils;
import net.plasmere.streamline.config.MessageConfUtils;
import net.plasmere.streamline.objects.savable.users.SavableUser;
import net.plasmere.streamline.utils.MessagingUtils;
import net.plasmere.streamline.utils.PlayerUtils;
import net.plasmere.streamline.utils.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeleteStatCommand extends Command implements TabExecutor {
    public DeleteStatCommand(String base, String perm, String[] aliases){
        super(base, perm, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsMore());
            return;
        }
        if (args.length > 1) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.bungeeNeedsLess());
            return;
        }

        SavableUser user = PlayerUtils.getOrGetSavableUser(args[0]);

        if (user == null) {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.noPlayer());
            return;
        }

//        PlayerUtils.removeStat(user);
        try {
            MessagingUtils.sendBUserMessage(sender, MessageConfUtils.deleteStatMessage()
                    .replace("%file_name%", user.file.getName())
            );
//            user.file.delete();
            user.dispose();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> uuids = new ArrayList<>();

        File folder = StreamLine.getInstance().getPlDir();
        File[] files = folder.listFiles();

        if (files == null) return uuids;

        for (File file : files) {
            if (! file.isDirectory()) continue;
            if (! file.getName().contains(ConfigUtils.consoleName) || ! file.getName().contains("-")) continue;

            uuids.add(file.getName().replace(".properties", ""));
        }

        if (args.length == 1) {
            return TextUtils.getCompletion(uuids, args[0]);
        }

        return new ArrayList<>();
    }
}
