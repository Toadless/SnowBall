package uk.toadl3ss.lavalite.entities.commandmeta.abs;

import com.mongodb.lang.NonNull;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import uk.toadl3ss.lavalite.entities.commandmeta.CommandFlags;

import javax.annotation.Nonnull;

public abstract class Command
{
    private final String name;
    private final String help;
    private CommandFlags flag;

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

    public void addFlag(CommandFlags flag)
    {
        this.flag = flag;
    }

    @Nonnull
    public CommandFlags getFlag()
    {
        return flag;
    }

    /* The abstract method to run the command */
    public abstract void run(@Nonnull String[] args, @NonNull GuildMessageReceivedEvent event, @NonNull String prefix);
}