package uk.toadl3ss.lavalite.entities.command.abs;

import com.mongodb.lang.NonNull;
import uk.toadl3ss.lavalite.entities.command.CommandEvent;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;

import javax.annotation.Nonnull;

/**
 * A class representing a command for use in the {@link uk.toadl3ss.lavalite.entities.command.CommandManager CommandManager}.
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

    /* Sets the command flag */
    public void addFlag(CommandFlags flag)
    {
        this.flag = flag;
    }

    /* Returns the command flag */
    public CommandFlags getFlag()
    {
        return flag;
    }

    /**
     *
     * @param ctx The {@link uk.toadl3ss.lavalite.entities.command.CommandEvent event} to use.
     */
    public abstract void run(@Nonnull CommandEvent ctx);
}