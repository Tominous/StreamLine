package net.plasmere.streamline.objects;

import net.plasmere.streamline.utils.UUIDFetcher;

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
        this(username, UUIDFetcher.getCachedUUID(username), reason, null);
    }

    public Punishment(String username, Date until){
        this(username, UUIDFetcher.getCachedUUID(username), "", until);
    }

    public Punishment(String username){
        this(username, UUIDFetcher.getCachedUUID(username), "", null);
    }

    public Punishment(UUID uuid, String reason){
        this(UUIDFetcher.getCachedName(uuid.toString()), uuid.toString(), reason, null);
    }

    public Punishment(UUID uuid, Date until){
        this(UUIDFetcher.getCachedName(uuid.toString()), uuid.toString(), "", until);
    }

    public Punishment(UUID uuid){
        this(UUIDFetcher.getCachedName(uuid.toString()), uuid.toString(), "", null);
    }
}
