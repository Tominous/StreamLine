package net.plasmere.streamline.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class PermissionHelper {
    public static boolean checkRoleIDPerms(MessageReceivedEvent event, String roleId){
        JDA jda = event.getJDA();
        for (Role role : Objects.requireNonNull(event.getMember()).getRoles()){
            if (role.equals(jda.getRoleById(roleId)))
                return true;
        }
        return false;
    }
}
