package net.toaddev.lavalite.entities.exception;

public class MusicException extends RuntimeException
{
    private final String text;

    public MusicException(String text)
    {
        super(text, null, true, false);
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
}