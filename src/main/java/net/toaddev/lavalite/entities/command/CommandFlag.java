package net.toaddev.lavalite.entities.command;

/**
 * An enum for flags related to {@link CommandManager command} execution.
 */
public enum CommandFlag
{
    /**
     * If the {@link CommandManager command} can only be used by developers.
     *
     * @see CommandEvent#isDeveloper().
     */
    DEVELOPER_ONLY,
    /**
     * If the {@link CommandManager command} can only be used by server admins.
     *
     * @see CommandEvent#isServerAdmin().
     */
    SERVER_ADMIN_ONLY,
    /**
     * If the {@link CommandManager command} is disabled.
     *
     */
    /**
     * If the {@link CommandManager commands} message needs to be deleted.
     */
    AUTO_DELETE_MESSAGE,

    /**
     * If the {@link CommandManager command} is disabled.
     */
    DISABLED;
}