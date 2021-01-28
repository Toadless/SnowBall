package net.toaddev.lavalite.entities.database.managers;

import net.toaddev.lavalite.main.Launcher;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class GuildDataManager
{
    public static String COLLECTION = "guilds";

    /**
     *
     * @param object The new object to insert
     */
    public static void insert(Document object) {
        Launcher.getDatabaseManager().runTask(database ->
        {
            database.getCollection(COLLECTION).insertOne(object);
        });
    }

    /**
     *
     * @param id The guilds id
     * @param object The object
     */
    public static void replace(long id, Document object)
    {
        Launcher.getDatabaseManager().runTask(database ->
        {
            database.getCollection(COLLECTION).replaceOne(eq("id", id), object);
        });
    }
}