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

import net.dv8tion.jda.api.GatewayEncoding;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.toaddev.snowball.data.Config;
import net.toaddev.snowball.event.ShardListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotController extends Launcher
{
    private static final Logger log = LoggerFactory.getLogger(BotController.class);
    private final int shardId;

    public BotController(int shardId)
    {
        this.shardId = shardId;
        shardListener = new ShardListener();

        log.info("Building shard " + shardId);
        try {
            boolean success = false;
            while (!success)
            {
                JDABuilder builder = JDABuilder.createDefault(Config.INS.getToken())
                        .enableIntents(
                                GatewayIntent.GUILD_MESSAGES,
                                GatewayIntent.GUILD_VOICE_STATES,
                                GatewayIntent.GUILD_EMOJIS
                        )
                        .disableCache(
                                CacheFlag.MEMBER_OVERRIDES
                        )
                        .enableCache(
                                CacheFlag.VOICE_STATE
                        )
                        .setActivity(Activity.playing("loading..."))
                        .setBulkDeleteSplittingEnabled(false)
                        .setCompression(Compression.NONE)
                        .addEventListeners(Launcher.getModules().getModules())
                        .addEventListeners(Launcher.getEventWaiter())
                        .addEventListeners(shardListener)
                        .setMemberCachePolicy(MemberCachePolicy.VOICE)
                        .setHttpClient(Launcher.getModules().getHttpClient())
                        .setStatus(OnlineStatus.DO_NOT_DISTURB)
                        .setGatewayEncoding(GatewayEncoding.ETF)
                        .setChunkingFilter(ChunkingFilter.NONE);
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