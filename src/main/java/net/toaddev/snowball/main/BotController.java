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
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BotController
{
    public static final Logger logger = LoggerFactory.getLogger(BotController.class);
    public static String version;
    public static boolean DATABASE_ENABLED = false;
    public static final long START_TIME = System.currentTimeMillis();
    public static final int UNKNOWN_SHUTDOWN_CODE = -991023;
    public static int shutdownCode = UNKNOWN_SHUTDOWN_CODE;
    public static final int SHARD_CREATION_SLEEP_INTERVAL = 5100;
    public static JDA jda;
    private static final ArrayList<BotController> shards = new ArrayList<>();
    private static AtomicInteger numShardsReady = new AtomicInteger(0);
    private static boolean hasReadiedOnce = false;
    private static Modules modules;
    private static MusicModule musicModule;
    private static String exampleConfigFile;
    private static EventWaiter eventWaiter;
    private static LocalDateTime startTimestamp;

    public BotController(Document xmlDoc) throws IOException, ParserConfigurationException, SAXException
    {
        startTimestamp = LocalDateTime.now();

        version = IOUtil.getXmlVal(xmlDoc, "Version");
        String configFile = IOUtil.getXmlVal(xmlDoc, "ConfigFile") + ".yml";
        exampleConfigFile = IOUtil.getXmlVal(xmlDoc, "ExampleConfigFile");

        Config.init(configFile);

        Constants.Init();

        DATABASE_ENABLED = Config.INS.getDatabase();

        eventWaiter = new EventWaiter();

        modules = new Modules();
        musicModule = modules.get(MusicModule.class);

        /* Init JDA */
        initBotShards();
    }

    public BotController()
    {
    }

    private static void initBotShards()
    {
        for(int i = Config.INS.getShardStart(); i < Config.INS.getNumShards(); i++)
        {
            try
            {
                shards.add(i, new SnowBall(i));
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

        logger.info("Received ready event for " + BotController.getInstance(readyEvent.getJDA()).getShardInfo().getShardString());

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
        for(BotController lch : shards) {
            lch.getJda().shutdown();
        }
        getJda().shutdown();
        System.exit(code);
    }

    public static JDA getJda()
    {
        return jda;
    }

    public static BotController getInstance(JDA jda)
    {
        int sId = jda.getShardInfo() == null ? 0 : jda.getShardInfo().getShardId();

        for(BotController lch : shards)
        {
            if(((SnowBall) lch).getShardId() == sId) {
                return lch;
            }
        }

        throw new IllegalStateException("Attempted to get instance for JDA shard that is not indexed");
    }

    public static BotController getInstance(int id)
    {
        return shards.get(id);
    }

    public JDA.ShardInfo getShardInfo()
    {
        int sId = jda.getShardInfo() == null ? 0 : jda.getShardInfo().getShardId();
        return new JDA.ShardInfo(sId, Config.INS.getNumShards());
    }

    public void revive() throws IOException, SAXException, ParserConfigurationException
    {
        jda.shutdown();
        logger.info("Reviving a shard");
        shards.set(getShardInfo().getShardId(), new SnowBall(getShardInfo().getShardId()));
    }

    public static List<BotController> getShards()
    {
        return shards;
    }

    public static List<Guild> getAllGuilds()
    {
        ArrayList<Guild> list = new ArrayList<>();

        for (BotController lch : shards) {
            list.addAll(lch.getJda().getGuilds());
        }

        return list;
    }

    public static Map<String, User> getAllUsersAsMap()
    {
        HashMap<String, User> map = new HashMap<>();

        for (BotController lch : shards) {
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