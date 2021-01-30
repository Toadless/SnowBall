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

package net.toaddev.lavalite.main;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.toaddev.lavalite.data.Config;
import net.toaddev.lavalite.event.EventLogger;
import net.toaddev.lavalite.event.ShardListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotController extends Launcher
{
    private static final Logger log = LoggerFactory.getLogger(BotController.class);
    private final int shardId;
    public BotController(int shardId, EventListener listener)
    {
        this.shardId = shardId;
        shardListener = new ShardListener();

        log.info("Building shard " + shardId);
        try {
            boolean success = false;
            while (!success)
            {
                JDABuilder builder = JDABuilder.createDefault(Config.INS.getToken())
                        .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES,GatewayIntent.GUILD_EMOJIS)
                        .disableCache(CacheFlag.MEMBER_OVERRIDES)
                        .enableCache(CacheFlag.VOICE_STATE)
                        .setActivity(Activity.playing("v" + Launcher.version + " " + "Starting"))
                        .setBulkDeleteSplittingEnabled(false)
                        .setCompression(Compression.NONE);
                if(listener != null)
                {
                    builder.addEventListeners(listener);
                    builder.addEventListeners(new EventLogger());
                    builder.addEventListeners(shardListener);
                } else
                    {
                    log.warn("Starting a shard without an event listener!");
                }
                if (Config.INS.getNumShards() > 1)
                {
                    builder.useSharding(shardId, Config.INS.getMaxShards());
                }
                jda = builder.build();
                success = true;
            }
        } catch (Exception e)
        {
            throw new RuntimeException("Failed to start JDA shard " + shardId, e);
        }
    }
    int getShardId()
    {
        return shardId;
    }
}