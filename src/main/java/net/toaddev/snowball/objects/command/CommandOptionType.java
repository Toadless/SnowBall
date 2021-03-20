package net.toaddev.snowball.objects.command;

import java.util.Arrays;

public enum CommandOptionType
{
    STRING(3),
    INTEGER(4),
    BOOLEAN(5),
    USER(6),
    UNKNOWN(0);

    private final int type;

    CommandOptionType(int type)
    {
        this.type = type;
    }

    public static CommandOptionType get(int type)
    {
        return Arrays.stream(values()).filter(t -> t.getType() == type).findFirst().orElse(null);
    }

    public int getType()
    {
        return this.type;
    }
}