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

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.toaddev.lavalite.data.Config;
import net.toaddev.lavalite.data.Constants;
import net.toaddev.lavalite.entities.database.IMongoTask;
import net.toaddev.lavalite.entities.database.managers.GuildDataManager;
import net.toaddev.lavalite.entities.modules.Module;
import net.toaddev.lavalite.main.Launcher;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DatabaseModule extends Module
{
    public static Map<Long, String> guildRegistry;

    // #####################################################
    // ##              Database Manager
    // #####################################################

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseModule.class);
    private MongoClient SYNC_CLIENT;
    private String databaseName;

    public DatabaseModule()
    {
        super("database");
        guildRegistry = new HashMap<>();
    }

    @Override
    public void onEnable()
    {
        MongoClientSettings.Builder builder = MongoClientSettings.builder();
        ConnectionString connString = new ConnectionString(Config.INS.getMongoUri());
        builder.applyConnectionString(connString);
        setDatabaseName(connString.getDatabase());
        SYNC_CLIENT = MongoClients.create(builder.build());
        LOGGER.info("Logged into database.");
    }

    /**
     *
     * @return The {@link DatabaseModule database}.
     */
    public MongoDatabase getDatabase()
    {
        return SYNC_CLIENT.getDatabase(databaseName);
    }

    /**
     *
     * @param task The {@link IMongoTask task} to run.
     */
    public void runTask(IMongoTask task)
    {
        task.run(SYNC_CLIENT.getDatabase(databaseName));
    }

    /**
     *
     * @param database The {@link DatabaseModule database} to set.
     */
    public void setDatabaseName(String database)
    {
        this.databaseName = database;
    }

    @Override
    public void onDisable()
    {
        SYNC_CLIENT.close();
    };


    // #####################################################
    // ##                Guild Registry
    // #####################################################

    /**
     *  The cache where we store the all of the guilds prefixes.
     */
    public void registerGuild(long id, String prefix)
    {
        guildRegistry.put(id, prefix);
    }

    /**
     *
     * @param id The {@link net.dv8tion.jda.api.entities.Guild guild} id.
     */
    public void createGuild(long id)
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
    public String getPrefix(long id)
    {
        String prefix = guildRegistry.get(id);
        if (prefix != null)
        {
            return prefix;
        }
        Document guildDocument = new Document("id", id);
        FindIterable<Document> guild = Launcher.getDatabaseModule().getDatabase().getCollection(GuildDataManager.COLLECTION).find(guildDocument);
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
    public void setPrefix(long id, String prefix)
    {
        Document newGuildDocument = new Document("id", id);
        newGuildDocument.append("prefix", prefix);
        GuildDataManager.replace(id, newGuildDocument);
        guildRegistry.remove(id);
        guildRegistry.put(id, prefix);
    }
}