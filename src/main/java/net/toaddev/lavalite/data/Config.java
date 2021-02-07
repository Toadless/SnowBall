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

package net.toaddev.lavalite.data;

import net.toaddev.lavalite.main.Launcher;
import net.toaddev.lavalite.util.IOUtil;
import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A class that pulls the config from the config yml file
 */
public class Config
{
    // ################################################################################
    // ##                     Login / credentials
    // ################################################################################
    private File config;
    private String fileName;
    private final Logger logger = LoggerFactory.getLogger(Config.class);
    public static Config INS;
    private String Prefix;
    private String Token;
    private Boolean Development;
    private String OwnerID;
    private Boolean Invite;
    private String Join;
    private int numShards;
    private int shardStart;
    private int maxShards;
    private Boolean database;
    private String mongoUri;
    private String mongoName;
    private String spotifyId;
    private String spotifyApiKey;

    /**
     *
     * @param file The config files name
     */
    public static void init(String file) throws IOException
    {
        INS = new Config(file);
    }

    /**
     *
     * @param file The config files name
     */
    private Config(String file) throws IOException
    {
        config = new File(file);
        initConfig();
    }

    private void initConfig() throws IOException
    {
        FileConfiguration config = new YamlConfiguration();
        try
        {
            config.load(this.config);
        } catch (InvalidConfigurationException | IOException e)
        {
            if (!this.config.exists())
            {
                String appConfig = IOUtil.getResourceFileContents(Launcher.getExampleConfigFile());
                this.config.createNewFile();
                FileWriter fileWriter = new FileWriter(this.config.getName());
                fileWriter.write(appConfig);
                fileWriter.close();
                logger.warn("Config file created. Please insert your bot token.");
                System.exit(0);
                return;
            }
            logger.error("Invalid config file.");
            return;
        }
        this.Token = config.getString("credentials.discordBotToken");
        this.Prefix = config.getString("config.prefix");
        this.Development = config.getBoolean("config.development");
        this.OwnerID = config.getString("config.ownerid");
        this.Invite = config.getBoolean("config.invite");
        this.Join = config.getString("config.join");
        this.numShards = config.getInt("config.shardCount");
        this.database = config.getBoolean("config.database");
        this.mongoUri = config.getString("credentials.mongoDBUri");
        this.mongoName = config.getString("credentials.mongoDBName");
        this.shardStart = config.getInt("config.shardStart");
        this.maxShards = config.getInt("config.maxShards");
        this.spotifyId = config.getString("credentials.spotifyId");
        this.spotifyApiKey = config.getString("credentials.spotifySecret");
    }

    /**
     *
     * @return The token to the discord api login with
     */
    public String getToken()
    {
        return Token;
    }

    /**
     *
     * @return The bots default prefix
     */
    public String getPrefix()
    {
        return Prefix;
    }

    /**
     *
     * @return If the bot is in development mode
     */
    public Boolean getDevelopment()
    {
        return Development;
    }

    /**
     *
     * @return The discord bots owner id
     */
    public String getOwnerID()
    {
        return OwnerID;
    }

    /**
     *
     * @return The guild invite message
     */
    public Boolean getInvite()
    {
        return Invite;
    }

    /**
     *
     * @return The guild join message
     */
    public String getJoin()
    {
        return Join;
    }

    /**
     *
     * @return The number of shards that will be built
     */
    public int getNumShards()
    {
        return numShards;
    }

    /**
     *
     * @return The database name
     */
    public Boolean getDatabase()
    {
        return database;
    }

    /**
     *
     * @return The mongodb connection uri
     */
    public String getMongoUri()
    {
        return mongoUri;
    }

    /**
     *
     * @return The mongodb database name
     */
    public String getMongoName()
    {
        return mongoName;
    }

    /**
     *
     * @return The shard builder staring point
     */
    public int getShardStart()
    {
        return shardStart;
    }

    /**
     *
     * @return The max amount of shards
     */
    public int getMaxShards()
    {
        return maxShards;
    }

    public String getSpotifyId()
    {
        return spotifyId;
    }

    public String getSpotifyApiKey()
    {
        return spotifyApiKey;
    }
}