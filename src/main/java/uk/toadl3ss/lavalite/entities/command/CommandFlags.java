package uk.toadl3ss.lavalite.entities.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public enum CommandFlags
{
    /**
     * If the {@link uk.toadl3ss.lavalite.entities.command.abs.Command command} can only be used by developers.
     *
     * @see uk.toadl3ss.lavalite.util.DiscordUtil#isOwner(User). 
     */
    DEVELOPER_ONLY,

    /**
     * If the {@link uk.toadl3ss.lavalite.entities.command.abs.Command command} can only be used by server owners.
     *
     * @see uk.toadl3ss.lavalite.util.DiscordUtil#isServerAdmin(Member) .
     */
    SERVER_ADMIN_ONLY,
    DEFAULT,

    /**
     * If the {@link uk.toadl3ss.lavalite.entities.command.abs.Command command} is disabled.
     *
     */
    DISABLED
}