package net.plasmere.streamline.objects;

import net.plasmere.streamline.utils.UUIDUtils;

import java.util.Date;
import java.util.UUID;

public class Punishment {
    public String username;
    public String uuid;
    public String reason;
    public Date until;

    public Punishment(String username, String uuid, String reason, Date until){
        this.username = username;
        this.uuid = uuid;
        this.reason = reason;
        this.until = until;
    }

    public Punishment(String username, String reason){
        this(username, UUIDUtils.getCachedUUID(username), reason, null);
    }

    public Punishment(String username, Date until){
        this(username, UUIDUtils.getCachedUUID(username), "", until);
    }

    public Punishment(String username){
        this(username, UUIDUtils.getCachedUUID(username), "", null);
    }

    public Punishment(UUID uuid, String reason){
        this(UUIDUtils.getCachedName(uuid.toString()), uuid.toString(), reason, null);
    }

    public Punishment(UUID uuid, Date until){
        this(UUIDUtils.getCachedName(uuid.toString()), uuid.toString(), "", until);
    }

    public Punishment(UUID uuid){
        this(UUIDUtils.getCachedName(uuid.toString()), uuid.toString(), "", null);
    }
}
