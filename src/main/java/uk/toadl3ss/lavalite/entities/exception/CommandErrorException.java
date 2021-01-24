package uk.toadl3ss.lavalite.entities.exception;

public class CommandErrorException extends CommandException
{
    public CommandErrorException(String text)
    {
        super(text);
    }
}