package uk.toadl3ss.lavalite.data.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseManager.class);
    private final MongoClient SYNC_CLIENT;
    private String databaseName;

    public DatabaseManager(String connectionString) {
        MongoClientSettings.Builder builder = MongoClientSettings.builder();
        ConnectionString connString = new ConnectionString(connectionString);
        builder.applyConnectionString(connString);
        setDatabaseName(connString.getDatabase());
        SYNC_CLIENT = MongoClients.create(builder.build());
        LOGGER.info("Logged into database.");
    }

    public MongoDatabase getDatabase() {
        return SYNC_CLIENT.getDatabase(databaseName);
    }

    public void runTask(IMongoTask task) {
        task.run(SYNC_CLIENT.getDatabase(databaseName));
    }

    public void insertDocument(String collection, Document document) {
        runTask(db -> {
            db.getCollection(collection).insertOne(document);
        });
    }

    public void setDatabaseName(String database) {
        this.databaseName = database;
    }
}