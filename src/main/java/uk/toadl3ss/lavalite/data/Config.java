package uk.toadl3ss.lavalite.data;

import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import uk.toadl3ss.lavalite.utils.Logger;

import java.io.File;
import java.io.IOException;

public class Config {
    // ################################################################################
    // ##                     Login / credentials
    // ################################################################################
    private File config;
    public static Config INS;
    private String Prefix;
    private String Token;
    private Boolean Development;
    private String OwnerID;
    private String Game;
    private String Status;
    private Boolean Invite;
    private String Join;
    private int numShards;
    private Boolean database;
    private String mongoUri;
    private String mongoName;

    private Config(String file) {
        config = new File(file);
        initConfig();
    }

    public static void init(String file) {
        INS = new Config(file);
    }

    private void initConfig() {
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(this.config);
        } catch (InvalidConfigurationException | IOException e) {
            Logger.error("Invalid application.yml");
            return;
        }
        this.Token = config.getString("credentials.discordBotToken");
        this.Prefix = config.getString("config.prefix");
        this.Development = config.getBoolean("config.development");
        this.OwnerID = config.getString("config.ownerid");
        this.Game = config.getString("config.game");
        this.Status = config.getString("config.status");
        this.Invite = config.getBoolean("config.invite");
        this.Join = config.getString("config.join");
        this.numShards = config.getInt("config.shardCount");
        this.database = config.getBoolean("config.database");
        this.mongoUri = config.getString("credentials.mongoDBUri");
        this.mongoName = config.getString("credentials.mongoDBName");
    }

    //Fetching the bots token
    public String getToken() {
        return Token;
    }

    //Fetching the default prefix
    public String getPrefix() {
        return Prefix;
    }

    //Fetching development state
    public Boolean getDevelopment() {
        return Development;
    }

    //Fetching the bots owner id
    public String getOwnerID() {
        return OwnerID;
    }

    //Fetching the status
    public String getGame() {
        return Game;
    }

    //Fetching the status
    public String getStatus() {
        return Status;
    }

    //Fetching the invite command status
    public Boolean getInvite() {
        return Invite;
    }

    //Fetching the default guild join message
    public String getJoin() {
        return Join;
    }

    //Fetching the amount of shards
    public int getNumShards() {
        return numShards;
    }

    //Fetching the database status
    public Boolean getDatabase() {
        return database;
    }

    //Fetching the database uri
    public String getMongoUri() {
        return mongoUri;
    }

    //Fetching the database name
    public String getMongoName() {
        return mongoName;
    }
}