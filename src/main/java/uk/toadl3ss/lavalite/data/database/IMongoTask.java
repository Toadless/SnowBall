package uk.toadl3ss.lavalite.data.database;

import com.mongodb.client.MongoDatabase;

public interface IMongoTask {
    public void run(MongoDatabase database);
}
