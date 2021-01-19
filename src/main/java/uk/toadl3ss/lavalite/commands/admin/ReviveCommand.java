package uk.toadl3ss.lavalite.commands.admin;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandOwnerRestricted;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;

public class ReviveCommand extends Command implements ICommandOwnerRestricted {
    @Override
    public void onInvoke(String[] args, GuildMessageReceivedEvent event, String prefix) {
        if (args.length < 2) {
            event.getChannel().sendMessage("Please provide a shard id to revive.").queue();
            return;
        }
        int shardId = Integer.parseInt(args[1]);
        event.getChannel().sendMessage("Reviving shard" + " " + shardId).queue();
        Launcher.getInstance(shardId).revive();
    }

    @Override
    public String getHelp() {
        return null;
    }
}