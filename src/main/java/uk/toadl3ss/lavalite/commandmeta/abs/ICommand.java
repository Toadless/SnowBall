package uk.toadl3ss.lavalite.commandmeta.abs;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface ICommand {
    /**
     *
     * @param args The commands arguments
     * @param event The event that gets called
     * @param prefix The prefix that the user provides
     */
    public abstract void onInvoke(String[] args, GuildMessageReceivedEvent event, String prefix);
}