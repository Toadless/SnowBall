/*
 *  Copyright (c) 2021 Toadless @ toaddev.net
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package net.toaddev.snowball.modules;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.toaddev.snowball.data.Config;
import net.toaddev.snowball.data.Constants;
import net.toaddev.snowball.entities.database.IMongoTask;
import net.toaddev.snowball.entities.module.Module;
import net.toaddev.snowball.main.BotController;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseModule extends Module
{
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseModule.class);
    public static String COLLECTION = "guilds";
    private boolean enabled;
    private MongoClient SYNC_CLIENT;
    private String databaseName;

    @Override
    public void onEnable()
    {
        enabled = Config.INS.getDatabase();

        if (!enabled)
        {
            LOG.info("Cancelling the launch of the database!");
            return;
        }

        MongoClientSettings.Builder builder = MongoClientSettings.builder();
        ConnectionString connString = new ConnectionString(Config.INS.getMongoUri());
        builder.applyConnectionString(connString);
        setDatabaseName(connString.getDatabase());
        setDatabaseName(Config.INS.getMongoName());
        SYNC_CLIENT = MongoClients.create(builder.build());
        LOG.info("Loaded database.");
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

    /**
     *
     * @param id The {@link net.dv8tion.jda.api.entities.Guild guild} id.
     */
    public void createGuild(long id)
    {
        Document guild = new Document("id", id);
        guild.append("prefix", Constants.GUILD_PREFIX);
        insert(guild);
    }

    /**
     *
     * @param id The {@link net.dv8tion.jda.api.entities.Guild guild} id.
     * @return The {@link net.dv8tion.jda.api.entities.Guild guild} prefix.
     */
    public String getPrefix(long id)
    {
        if (!enabled)
        {
            return Constants.GUILD_PREFIX;
        }

        Document guildDocument = new Document("id", id);
        FindIterable<Document> guild = BotController.getModules().get(DatabaseModule.class).getDatabase().getCollection(COLLECTION).find(guildDocument);
        if (guild.first() == null)
        {
            createGuild(id);
            return Constants.GUILD_PREFIX;
        }
        return (String) guild.first().get("prefix");
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
        replace(id, newGuildDocument);
    }

    public void insertSettingsIfNotExists(long id)
    {
        if (!enabled)
        {
            return;
        }
        Document guildDocument = new Document("id", id);
        FindIterable<Document> guild = BotController.getModules().get(DatabaseModule.class).getDatabase().getCollection(COLLECTION).find(guildDocument);
        if (guild.first() == null)
        {
            createGuild(id);
            return;
        }
        return;
    }


    /**
     *
     * @param object The new object to insert
     */
    public void insert(Document object) {
        BotController.getModules().get(DatabaseModule.class).runTask(database ->
        {
            database.getCollection(COLLECTION).insertOne(object);
        });
    }

    /**
     *
     * @param id The guilds id
     * @param object The object
     */
    public void replace(long id, Document object)
    {
        BotController.getModules().get(DatabaseModule.class).runTask(database ->
        {
            database.getCollection(COLLECTION).replaceOne(eq("id", id), object);
        });
    }

    public void remove(long id)
    {
        BotController.getModules().get(DatabaseModule.class).runTask(database ->
        {
            database.getCollection(COLLECTION).deleteOne(eq("id", id));
        });
    }

    @Override
    public void onDisable()
    {
        if (!enabled)
        {
            return;
        }

        SYNC_CLIENT.close();
    }
}