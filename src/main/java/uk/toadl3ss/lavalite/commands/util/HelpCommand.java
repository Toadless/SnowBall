package uk.toadl3ss.lavalite.commands.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;

public class HelpCommand extends Command {
    @Override
    public void onInvoke(String[] args, MessageReceivedEvent event, String prefix) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Help Page");
        embedBuilder.addField("Join", "Joins your voice channel", false);
        embedBuilder.addField("Leave", "Leaves your voice channel", false);
        embedBuilder.addField("Nowplaying", "Displays what is currently playing", false);
        embedBuilder.addField("Play", "Plays the provided song", false);
        embedBuilder.addField("Queue", "Displays the current players queue", false);
        embedBuilder.addField("Loop", "Loops the current playing song", false);
        embedBuilder.addField("Skip", "Skips the current playing song", false);
        embedBuilder.addField("Stop", "Stops the current playing song", false);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}