package net.toaddev.lavalite.entities.database;

import com.mongodb.client.MongoDatabase;

public interface IMongoTask
{
    /**
     *
     * @param database The {@link DatabaseManager database} to use.
     */
    public void run(MongoDatabase database);
}
