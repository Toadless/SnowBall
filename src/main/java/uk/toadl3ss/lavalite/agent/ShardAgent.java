package uk.toadl3ss.lavalite.agent;

import net.dv8tion.jda.api.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.toadl3ss.lavalite.data.Config;
import uk.toadl3ss.lavalite.event.ShardListener;
import uk.toadl3ss.lavalite.main.Launcher;

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