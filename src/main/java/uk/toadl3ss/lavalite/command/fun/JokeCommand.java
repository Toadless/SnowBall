package uk.toadl3ss.lavalite.command.fun;

import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.command.CommandEvent;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.entities.command.abs.Command;
import uk.toadl3ss.lavalite.util.Http;

public class JokeCommand extends Command
{
    public JokeCommand()
    {
        super("joke", null);
        addFlag(CommandFlags.DEFAULT);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        String joke = Http.getReq("http://api.icndb.com/jokes/random");
        if (joke == null)
        {
            ctx.getChannel().sendMessage("Unable to request a joke.").queue();
            return;
        }
        DataObject object = DataObject.fromJson(joke);
        DataObject value = object.getObject("value");
        Object result = value.get("joke");

        result = result.toString().replaceAll("Chuck Norris", ctx.getMember().getUser().getName());
        result = result.toString().replaceAll("&quot;", "\"");

        ctx.getChannel().sendMessage(result.toString()).queue();
    }
}