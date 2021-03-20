package net.toaddev.snowball.objects.command.options;

import net.toaddev.snowball.objects.command.CommandOption;
import net.toaddev.snowball.objects.command.CommandOptionType;
import net.toaddev.snowball.objects.exception.CommandException;

public class CommandOptionString extends CommandOption<String>
{

    public CommandOptionString(String name, String description)
    {
        super(CommandOptionType.STRING, name, description);
    }

    @Override
    public String parseValue(Object value)
    {
        try
        {
            return (String) value;
        } catch (ClassCastException e)
        {
            throw new CommandException("Failed to parse " + value + " as string");
        }
    }

}
