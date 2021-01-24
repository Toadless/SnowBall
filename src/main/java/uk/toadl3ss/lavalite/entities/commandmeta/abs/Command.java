package uk.toadl3ss.lavalite.entities.commandmeta.abs;

import com.mongodb.lang.NonNull;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import uk.toadl3ss.lavalite.entities.commandmeta.CommandType;
import uk.toadl3ss.lavalite.perms.PermissionLevel;

import javax.annotation.Nonnull;

public abstract class Command
{
    private final String name;
    private final String help;
    private final PermissionLevel permissionNode;
    private final CommandType commandType;

    public Command(@NonNull String name, @NonNull String help, @NonNull PermissionLevel permissionNode, CommandType commandType)
    {
        this.name = name;
        this.help = help;
        this.permissionNode = permissionNode;
        this.commandType = commandType;
    }

    /* Returns the commands name */
    @NonNull
    public String getName()
    {
        return name;
    }

    /* Returns the commands help */
    public String getHelp()
    {
        return help;
    }

    /* Returns the commands PermissionNode */
    @NonNull
    public PermissionLevel getPermissionNode()
    {
        return permissionNode;
    }

    @NonNull
    public CommandType getCommandType()
    {
        return commandType;
    }

    /* The abstract method to run the command */
    public abstract void run(@Nonnull String[] args, @NonNull GuildMessageReceivedEvent event, @NonNull String prefix);
}