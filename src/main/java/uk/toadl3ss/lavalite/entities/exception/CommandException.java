package uk.toadl3ss.lavalite.entities.exception;

import uk.toadl3ss.lavalite.entities.command.abs.Command;

public class CommandException extends RuntimeException
{
    private final String text;
    private final Command command;

    public CommandException(Command command)
    {
        super("An exception occurred in command " + command.getName(), null, true, false);
        this.text = "An exception occurred in command " + command.getName();
        this.command = command;
    }

    public CommandException(String text)
    {
        super(text, null, true, false);
        this.text = text;
        this.command = null;
    }

    public String getText()
    {
        return text;
    }

    public Command getCommand()
    {
        return command;
    }
}