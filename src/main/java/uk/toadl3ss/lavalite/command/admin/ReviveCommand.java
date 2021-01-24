package uk.toadl3ss.lavalite.command.admin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.perms.PermissionLevel;

public class ReviveCommand extends Command {
    public ReviveCommand()
    {
        super("revive", null, PermissionLevel.BOT_ADMIN);
    }

    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        if (args.length < 2) {
            event.getChannel().sendMessage("Please provide a shard id to revive.").queue();
            return;
        }
        int shardId = Integer.parseInt(args[1]);
        event.getChannel().sendMessage("Reviving shard" + " " + shardId).queue();
        Launcher.getInstance(shardId).revive();
    }
}