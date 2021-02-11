package net.toaddev.snowball.modules;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.toaddev.snowball.api.API;
import net.toaddev.snowball.data.Config;
import net.toaddev.snowball.entities.module.Module;
import org.jetbrains.annotations.NotNull;

public class BotListModules extends Module
{
    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        updateStats();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event)
    {
        updateStats();
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event)
    {
        updateStats();
    }

    private void updateStats()
    {
        var guildCount = getTotalGuilds();
        var requestModule = this.modules.get(RequestModule.class);
        if(!Config.INS.getTopGGToken().isBlank())
        {
            requestModule.updateStats(API.TOP_GG, guildCount, Config.INS.getTopGGToken());
        }
    }

    private int getTotalGuilds()
    {
        return (int) this.modules.getJDA().getGuildCache().size();
    }
}