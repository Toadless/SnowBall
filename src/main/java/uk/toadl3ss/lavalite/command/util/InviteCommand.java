package uk.toadl3ss.lavalite.command.util;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.command.CommandEvent;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.entities.command.abs.Command;
import uk.toadl3ss.lavalite.data.Constants;

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