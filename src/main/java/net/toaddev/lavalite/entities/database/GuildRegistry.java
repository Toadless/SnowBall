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

package net.toaddev.lavalite.entities.database;

import com.mongodb.client.FindIterable;
import net.toaddev.lavalite.main.Launcher;
import org.bson.Document;

import net.toaddev.lavalite.data.Constants;
import net.toaddev.lavalite.entities.database.managers.GuildDataManager;

import java.util.HashMap;
import java.util.Objects;

public class GuildRegistry
{
    /**
     *  The cache where we store the all of the guilds prefixes.
     */
    public static HashMap<Long, String> guildRegistry = new HashMap<>();
    public static void registerGuild(long id, String prefix)
    {
        guildRegistry.put(id, prefix);
    }

    /**
     *
     * @param id The {@link net.dv8tion.jda.api.entities.Guild guild} id.
     */
    public static void createGuild(long id)
    {
        guildRegistry.put(id, Constants.GUILD_PREFIX);
        Document guild = new Document("id", id);
        guild.append("prefix", Constants.GUILD_PREFIX);
        GuildDataManager.insert(guild);
    }

    /**
     *
     * @param id The {@link net.dv8tion.jda.api.entities.Guild guild} id.
     * @return The {@link net.dv8tion.jda.api.entities.Guild guild} prefix.
     */
    public static String getPrefix(long id)
    {
        String prefix = guildRegistry.get(id);
        if (prefix != null)
        {
            return prefix;
        }
        Document guildDocument = new Document("id", id);
        FindIterable<Document> guild = Launcher.getDatabaseManager().getDatabase().getCollection(GuildDataManager.COLLECTION).find(guildDocument);
        if (guild.first() == null)
        {
            createGuild(id);
            return Constants.GUILD_PREFIX;
        }
        prefix = (String) Objects.requireNonNull(guild.first()).get("prefix");
        registerGuild(id, prefix);
        return prefix;
    }

    /**
     *
     * @param id The {@link net.dv8tion.jda.api.entities.Guild guild} id.
     * @param prefix The {@link net.dv8tion.jda.api.entities.Guild guild} new prefix.
     */
    public static void setPrefix(long id, String prefix)
    {
        Document newGuildDocument = new Document("id", id);
        newGuildDocument.append("prefix", prefix);
        GuildDataManager.replace(id, newGuildDocument);
        guildRegistry.remove(id);
        guildRegistry.put(id, prefix);
    }
}