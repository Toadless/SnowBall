package net.toaddev.lavalite.entities.command.abs;

import com.mongodb.lang.NonNull;
import net.toaddev.lavalite.entities.command.CommandFlags;
import net.toaddev.lavalite.entities.command.CommandEvent;
import net.toaddev.lavalite.entities.command.CommandManager;

import javax.annotation.Nonnull;

/**
 * A class representing a command for use in the {@link CommandManager CommandManager}.
 *
 */
public abstract class Command
{
    private final String name;
    private final String help;
    private CommandFlags flag;

    /**
     *
     * @param name The commands name
     * @param help The commands help page
     */
    public Command(@NonNull String name, @NonNull String help)
    {
        this.name = name;
        this.help = help;
        this.flag = null;
    }

    /**
     *
     * @return The command name
     */
    @NonNull
    public String getName()
    {
        return name;
    }

    /**
     *
     * @return Returns the string for the help page
     */
    public String getHelp()
    {
        return help;
    }

    /**
     *
     * @param flag The {@link CommandFlags flag} to use.
     */
    public void addFlag(CommandFlags flag)
    {
        this.flag = flag;
    }

    /**
     *
     * @return The {@link CommandFlags flag} to use.
     */
    public CommandFlags getFlag()
    {
        return flag;
    }

    /**
     *
     * @param ctx The {@link CommandEvent event} to use.
     */
    public abstract void run(@Nonnull CommandEvent ctx);
}