package net.toaddev.snowball.objects.command.options;

import net.toaddev.snowball.objects.command.CommandOption;
import net.toaddev.snowball.objects.command.CommandOptionType;
import net.toaddev.snowball.objects.exception.CommandException;

public class CommandOptionInteger extends CommandOption<Integer>
{

    public CommandOptionInteger(String name, String description)
    {
        super(CommandOptionType.INTEGER, name, description);
    }

    @Override
    public Integer parseValue(Object value)
    {
        try
        {
            return (int) value;
        } catch (ClassCastException e)
        {
            throw new CommandException("Failed to parse " + value + " as integer");
        }
    }

}
