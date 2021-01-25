package uk.toadl3ss.lavalite.entities.database.managers;

import org.bson.Document;
import uk.toadl3ss.lavalite.main.Launcher;

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