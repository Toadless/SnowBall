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
     * @return The {@link DatabaseManager database}.
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
     * @param database The {@link DatabaseManager database} to set.
     */
    public void setDatabaseName(String database)
    {
        this.databaseName = database;
    }
}