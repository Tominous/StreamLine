package net.plasmere.streamline.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.plasmere.streamline.config.ConfigUtils;

import java.util.Objects;

public class PermissionHelper {
    public static boolean checkRoleIDPerms(MessageReceivedEvent event, String roleId){
        long id = 0L;
        try {
            id = Long.parseLong(roleId);
        } catch (Exception e) {
            return true;
        }

        if (! ConfigUtils.moduleDEnabled) {
            return false;
        }


        JDA jda = event.getJDA();
        for (Role role : Objects.requireNonNull(event.getMember()).getRoles()){
            if (role.equals(jda.getRoleById(id))) {
                return true;
            }
        }
        return false;
    }
}
