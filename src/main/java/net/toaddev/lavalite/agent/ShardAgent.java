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

package net.toaddev.lavalite.agent;

import net.dv8tion.jda.api.JDA;
import net.toaddev.lavalite.main.Launcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.toaddev.lavalite.data.Config;
import net.toaddev.lavalite.event.ShardListener;

import java.util.List;

public class ShardAgent extends Thread
{
    private static final Logger log = LoggerFactory.getLogger(ShardAgent.class);
    private static final int INTERVAL_MILLIS = 10000;
    private static final int ACCEPTABLE_SILENCE = getAcceptableSilenceThreshold();
    @Override
    public void run()
    {
        log.info("Started shard agent");
        while (true)
        {
            try
            {
                inspect();
                sleep(INTERVAL_MILLIS);
            } catch (Exception e)
            {
                log.error("Caught an exception while trying kill dead shards!", e);
                try
                {
                    sleep(1000);
                } catch (InterruptedException e1)
                {
                    log.error("Interrupted while sleeping after an exception in shard watchdog", e);
                }
            }
        }
    }

    private void inspect() throws InterruptedException
    {
        List<Launcher> shards = Launcher.getShards();

        for(Launcher shard : shards)
        {
            ShardListener listener = shard.getShardListener();

            long diff = System.currentTimeMillis() - listener.getLastEventTime();

            if(diff > ACCEPTABLE_SILENCE)
            {
                if (shard.getJda().getStatus() == JDA.Status.SHUTDOWN)
                {
                    log.warn("Did not revive shard " + shard.getShardInfo() + " because it was shut down!");
                } else if(listener.getEventCount() < 100)
                {
                    log.warn("Did not revive shard " + shard.getShardInfo() + " because it did not receive enough events since construction!");
                } else
                    {
                    log.warn("Reviving shard " + shard.getShardInfo() + " after " + (diff / 1000) +
                            " seconds of no events. Last event received was " + listener.getLastEvent());
                    shard.revive();
                    sleep(5000);
                }
            }
        }
    }

    private static int getAcceptableSilenceThreshold()
    {
        if(Config.INS.getDevelopment()) {
            return Integer.MAX_VALUE;
        }
        return Config.INS.getNumShards() != 1 ? 30 * 1000 : 600 * 1000;
    }
}