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