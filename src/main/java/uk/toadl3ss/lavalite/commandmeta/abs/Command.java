package uk.toadl3ss.lavalite.commandmeta.abs;

import com.mongodb.lang.NonNull;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import uk.toadl3ss.lavalite.perms.PermissionLevel;

import javax.annotation.Nonnull;

public abstract class Command {
    private final String name;
    private final String help;
    private final PermissionLevel permissionNode;

    public Command(@NonNull String name, @NonNull String help, @NonNull PermissionLevel permissionNode) {
        this.name = name;
        this.help = help;
        this.permissionNode = permissionNode;
    }

    /* Returns the commands name */
    @NonNull
    public String getName() {
        return name;
    }

    /* Returns the commands help */
    public String getHelp() {
        return help;
    }

    /* Returns the commands PermissionNode */
    @NonNull
    public PermissionLevel getPermissionNode() {
        return permissionNode;
    }

    /* The abstract method to run the command */
    public abstract void run(@Nonnull String[] args, @NonNull GuildMessageReceivedEvent event, @NonNull String prefix);
}