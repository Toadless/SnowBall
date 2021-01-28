package net.toaddev.lavalite.entities.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.toaddev.lavalite.entities.command.abs.Command;
import net.toaddev.lavalite.util.DiscordUtil;

public enum CommandFlags
{
    /**
     * If the {@link Command command} can only be used by developers.
     *
     * @see DiscordUtil#isOwner(User).
     */
    DEVELOPER_ONLY,

    /**
     * If the {@link Command command} can only be used by server owners.
     *
     * @see DiscordUtil#isServerAdmin(Member) .
     */
    SERVER_ADMIN_ONLY,
    DEFAULT,

    /**
     * If the {@link Command command} is disabled.
     *
     */
    DISABLED
}