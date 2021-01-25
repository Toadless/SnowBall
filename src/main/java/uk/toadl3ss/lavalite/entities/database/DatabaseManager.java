package uk.toadl3ss.lavalite.entities.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseManager.class);
    private final MongoClient SYNC_CLIENT;
    private String databaseName;

    /**
     *
     * @param connectionString The mongodb connection uri
     */
    public DatabaseManager(String connectionString)
    {
        MongoClientSettings.Builder builder = MongoClientSettings.builder();
        ConnectionString connString = new ConnectionString(connectionString);
        builder.applyConnectionString(connString);
        setDatabaseName(connString.getDatabase());
        SYNC_CLIENT = MongoClients.create(builder.build());
        LOGGER.info("Logged into database.");
    }

    /**
     *
     * @return The {@link uk.toadl3ss.lavalite.entities.database.DatabaseManager database}.
     */
    public MongoDatabase getDatabase()
    {
        return SYNC_CLIENT.getDatabase(databaseName);
    }

    /**
     *
     * @param task The {@link uk.toadl3ss.lavalite.entities.database.IMongoTask task} to run.
     */
    public void runTask(IMongoTask task)
    {
        task.run(SYNC_CLIENT.getDatabase(databaseName));
    }

    /**
     *
     * @param database The {@link uk.toadl3ss.lavalite.entities.database.DatabaseManager database} to set.
     */
    public void setDatabaseName(String database)
    {
        this.databaseName = database;
    }
}