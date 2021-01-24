package uk.toadl3ss.lavalite.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.toadl3ss.lavalite.data.Config;

public class DiscordUtil
{
    private static final Logger log = LoggerFactory.getLogger(DiscordUtil.class);
    private static final int embedColor = 0xfc0303;

    private DiscordUtil() {

    }

    public static String getOwnerId()
    {
        return Config.INS.getOwnerID();
    }

    public static Boolean isOwner(User user)
    {
        String userId = user.getId();
        String ownerId = getOwnerId();
        if (userId.equals(ownerId))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static Boolean isServerAdmin(Member member)
    {
        if (member.hasPermission(Permission.MANAGE_SERVER))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static int getEmbedColor()
    {
        return embedColor;
    }
}