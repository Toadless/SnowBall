/*
 *  MIT License
 *
 *  Copyright (c) 2021 Toadless @ toaddev.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of Lavalite and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of Lavalite, and to permit persons to whom Lavalite is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Lavalite.
 *
 * LAVALITE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package net.toaddev.lavalite.modules;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.toaddev.lavalite.data.Config;
import net.toaddev.lavalite.data.Constants;
import net.toaddev.lavalite.entities.module.Module;
import net.toaddev.lavalite.main.Launcher;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SettingsModule extends Module
{
    private static final Logger LOG = LoggerFactory.getLogger(SettingsModule.class);
    private Cache<Long, String> guildSettings;

    @Override
    public void onEnable()
    {
        this.guildSettings = Caffeine.newBuilder()
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event)
    {
        if (!Config.INS.getDatabase())
        {
            return;
        }
        insertSettingsIfNotExists(event.getGuild());
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event)
    {
        if (!Config.INS.getDatabase())
        {
            return;
        }
        insertSettingsIfNotExists(event.getGuild());
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event)
    {
        if (!Config.INS.getDatabase())
        {
            return;
        }
        cleanupAllSettings(event.getGuild().getIdLong());
    }

    public void cleanupAllSettings(long guildId)
    {
        LOG.info("Cleaning up guild: {}", guildId);
        DatabaseModule databaseModule = Launcher.getModules().get(DatabaseModule.class);
        databaseModule.remove(guildId);
    }

    public void insertSettingsIfNotExists(Guild guild)
    {
        DatabaseModule databaseModule = Launcher.getModules().get(DatabaseModule.class);
        databaseModule.insertSettingsIfNotExists(guild.getIdLong());
    }

    public String getGuildPrefix(long id)
    {
        if (!Config.INS.getDatabase())
        {
            return Constants.GUILD_PREFIX;
        }

        String prefix = guildSettings.getIfPresent(id);
        if (prefix == null)
        {
            DatabaseModule databaseModule = Launcher.getModules().get(DatabaseModule.class);
            String p = databaseModule.getPrefix(id);
            guildSettings.put(id, p);
            return p;
        }
        return prefix;
    }

    /**
     *
     * @param id The guilds id.
     * @param prefix The guilds new prefix.
     */
    public void setGuildPrefix(long id, String prefix)
    {
        if (!Config.INS.getDatabase())
        {
            return;
        }

        guildSettings.put(id, prefix);

        DatabaseModule databaseModule = Launcher.getModules().get(DatabaseModule.class);

        databaseModule.setPrefix(id, prefix);
        return;
    }

    @Override
    public void onDisable()
    {
        guildSettings.cleanUp();
    }
}