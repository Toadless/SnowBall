/*
 *  Copyright (c) 2021 Toadless @ toaddev.net
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package net.toaddev.snowball.main;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.toaddev.snowball.entities.module.Module;
import net.toaddev.snowball.services.Modules;
import net.toaddev.snowball.modules.MusicModule;
import net.toaddev.snowball.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.toaddev.snowball.data.Config;
import net.toaddev.snowball.data.Constants;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class Launcher
{
    public static final Logger logger = LoggerFactory.getLogger(Launcher.class);
    public static String version;
    public static boolean DATABASE_ENABLED = false;
    public static final long START_TIME = System.currentTimeMillis();
    public static final int UNKNOWN_SHUTDOWN_CODE = -991023;
    public static int shutdownCode = UNKNOWN_SHUTDOWN_CODE;
    public static final int SHARD_CREATION_SLEEP_INTERVAL = 5100;
    public static JDA jda;
    private static final ArrayList<Launcher> shards = new ArrayList<>();
    private static AtomicInteger numShardsReady = new AtomicInteger(0);
    private static boolean vanity = true;
    private static boolean hasReadiedOnce = false;
    private static Modules modules;
    private static MusicModule musicModule;
    private static String exampleConfigFile;
    private static EventWaiter eventWaiter;
    private static LocalDateTime startTimestamp;

    private static String getVersionInfo()
    {
        String indentation = "\t";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z");
        String startTime = format.format(new Date());
        int Cores = Runtime.getRuntime().availableProcessors();
        String versionInfo = new StringBuilder()
                .append(indentation + "Version:        ").append(Launcher.version + "\n")
                .append(indentation + "Cores:          ").append(Cores + "\n")
                .append(indentation + "Author:         ").append("Toadless" + "\n")
                .append(indentation + "StartTime:      ").append(startTime + "\n")
                .append(indentation + "JVM:            ").append(System.getProperty("java.version") + "\n")
                .append(indentation + "Lavaplayer      ").append(PlayerLibrary.VERSION + "\n")
                .append(indentation + "JDA             ").append(JDAInfo.VERSION + "\n")
                .append("\n")
                .toString();
        if (vanity)
        {
            versionInfo =
                    getVanity() +
                    "\n" +
                    "\n" +
                    versionInfo
            ;
        }
        return versionInfo;
    }
    private static String getVanity()
    {
        String red = "[31m";
        String green = "[32m";
        String defaultC = "[0m";

        String vanity = ("\ng       .                           r_           _ _  g__ _ _\n" +
                "g      /\\\\  r___ _ __   _____      _| |__   __ _| | | \\ g\\ \\ \\\n" +
                "g     ( ( )r/ __| '_ \\ / _ \\ \\ r/r\\ / / '_ \\ / _` | | |  g\\ \\ \\ \\\n" +
                "g      \\\\/ r\\__ \\ | | | (_) \\ V  V /| |_) | (_| | | |   g) ) ) )\n" +
                "g       '  r|___/_| |_|\\___/ \\_/\\_/ |_.__/ \\__,_|_|_|  g/ / / /\n" +
                "g    d================================================g/_/_/_/d");

        vanity = vanity.replace("r", red);
        vanity = vanity.replace("g", green);
        vanity = vanity.replace("d", defaultC);
        return vanity;
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException
    {
        var javaVersionMajor = Runtime.version().feature();
        if (javaVersionMajor != 15)
        {
            logger.warn("\n\t\t __      ___   ___ _  _ ___ _  _  ___ \n" +
                    "\t\t \\ \\    / /_\\ | _ \\ \\| |_ _| \\| |/ __|\n" +
                    "\t\t  \\ \\/\\/ / _ \\|   / .` || || .` | (_ |\n" +
                    "\t\t   \\_/\\_/_/ \\_\\_|_\\_|\\_|___|_|\\_|\\___|\n" +
                    "\t\t                                      ");
            logger.warn("Snowball only officially supports Java 15. You are running Java {}", Runtime.version());
        }

        var sa = new SpringApplication(Launcher.class);
        sa.setBannerMode(Banner.Mode.OFF);
        sa.run(args);

        startTimestamp = LocalDateTime.now();

        String xml = IOUtil.getResourceFileContents("snowball/snowball.xml");
        Document xmlDoc = IOUtil.convertStringToXMLDocument(xml);

        version = IOUtil.getXmlVal(xmlDoc, "Version");
        String configFile = IOUtil.getXmlVal(xmlDoc, "ConfigFile") + ".yml";
        exampleConfigFile = IOUtil.getXmlVal(xmlDoc, "ExampleConfigFile");

        Config.init(configFile);

        if (Config.INS.getDevelopment())
        {
            version = version + " " + "DEV";
        }
        if (args.length > 0 &&
                (args[0].equals("-v".toLowerCase()) || args[0].equals("--version".toLowerCase(Locale.ROOT))))
        {
            vanity = false;
            logger.info(getVersionInfo());
            return;
        }
        logger.info(getVersionInfo());

        Constants.Init();

        DATABASE_ENABLED = Config.INS.getDatabase();

        eventWaiter = new EventWaiter();

        modules = new Modules();
        musicModule = modules.get(MusicModule.class);

        /* Init JDA */
        initBotShards();

        logger.info("You can safely ignore the big red warning about illegal reflection.");
    }

    private static void initBotShards()
    {
        for(int i = Config.INS.getShardStart(); i < Config.INS.getNumShards(); i++)
        {
            try
            {
                shards.add(i, new BotController(i));
            } catch (Exception e)
            {
                logger.error("Caught an exception while starting shard " + i + "!", e);
                numShardsReady.getAndIncrement();
            }
            try
            {
                Thread.sleep(SHARD_CREATION_SLEEP_INTERVAL);
            } catch (InterruptedException e)
            {
                throw new RuntimeException("Got interrupted while setting up bot shards!", e);
            }
        }
        logger.info(shards.size() + " shards have been constructed");
        logger.info("Snowball ready in {}ms", System.currentTimeMillis() - START_TIME);
    }

    public static void onInit(ReadyEvent readyEvent)
    {
        if (!hasReadiedOnce)
        {
            numShardsReady.incrementAndGet();
            hasReadiedOnce = false;
        }

        logger.info("Received ready event for " + Launcher.getInstance(readyEvent.getJDA()).getShardInfo().getShardString());

        int ready = numShardsReady.get();
        if (ready == Config.INS.getNumShards())
        {
            logger.info("All " + ready + " shards are ready.");
        }
    }

    public static void shutdown(int code)
    {
        logger.info("Shutting down with exit code " + code);
        modules.modules.forEach(Module::onDisable);
        shutdownCode = code;
        for(Launcher lch : shards) {
            lch.getJda().shutdown();
        }
        getJda().shutdown();
        System.exit(code);
    }

    public static JDA getJda()
    {
        return jda;
    }

    public static Launcher getInstance(JDA jda)
    {
        int sId = jda.getShardInfo() == null ? 0 : jda.getShardInfo().getShardId();

        for(Launcher lch : shards)
        {
            if(((BotController) lch).getShardId() == sId) {
                return lch;
            }
        }

        throw new IllegalStateException("Attempted to get instance for JDA shard that is not indexed");
    }

    public static Launcher getInstance(int id)
    {
        return shards.get(id);
    }

    public JDA.ShardInfo getShardInfo()
    {
        int sId = jda.getShardInfo() == null ? 0 : jda.getShardInfo().getShardId();
        return new JDA.ShardInfo(sId, Config.INS.getNumShards());
    }

    public void revive()
    {
        jda.shutdown();
        logger.info("Reviving a shard");
        shards.set(getShardInfo().getShardId(), new BotController(getShardInfo().getShardId()));
    }

    public static List<Launcher> getShards()
    {
        return shards;
    }

    public static List<Guild> getAllGuilds()
    {
        ArrayList<Guild> list = new ArrayList<>();

        for (Launcher lch : shards) {
            list.addAll(lch.getJda().getGuilds());
        }

        return list;
    }

    public static Map<String, User> getAllUsersAsMap()
    {
        HashMap<String, User> map = new HashMap<>();

        for (Launcher lch : shards) {
            for (User usr : lch.getJda().getUsers())
            {
                map.put(usr.getId(), usr);
            }
        }

        return map;
    }

    public static long getAllShards()
    {
        long size = getJda().getShardInfo().getShardTotal();
        return size;
    }

    public static String getExampleConfigFile()
    {
        return exampleConfigFile;
    }

    public static Modules getModules()
    {
        return modules;
    }

    public static MusicModule getMusicModule()
    {
        return musicModule;
    }

    public static EventWaiter getEventWaiter()
    {
        return eventWaiter;
    }

    public static LocalDateTime getStartTimestamp()
    {
        return startTimestamp;
    }
}