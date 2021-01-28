package net.toaddev.lavalite.command.util;

import net.toaddev.lavalite.entities.command.CommandFlags;
import net.toaddev.lavalite.entities.command.abs.Command;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandEvent;
import net.toaddev.lavalite.data.Constants;

public class InviteCommand extends Command
{

    public InviteCommand()
    {
        super("invite", null);
        addFlag(CommandFlags.DEFAULT);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        if (!Constants.invite)
        {
            return;
        }
        String str = "https://discord.com/api/oauth2/authorize?client_id=" + ctx.getJDA().getSelfUser().getId() + "&permissions=267911025&scope=bot";
        ctx.getChannel().sendMessageFormat("My invite: %s", str).queue();
    }
}