package uk.toadl3ss.lavalite.events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class UserListener {

    public abstract void onGuildMessageReceived(GuildMessageReceivedEvent event);

}
