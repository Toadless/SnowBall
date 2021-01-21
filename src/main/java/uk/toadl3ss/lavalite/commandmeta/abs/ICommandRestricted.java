package uk.toadl3ss.lavalite.commandmeta.abs;

import uk.toadl3ss.lavalite.perms.PermissionLevel;

/**
 *  This interface is for commands that should only be executed by the bot owners
 */
public interface ICommandRestricted extends ICommand {
    public PermissionLevel getMinimumPerms();
}