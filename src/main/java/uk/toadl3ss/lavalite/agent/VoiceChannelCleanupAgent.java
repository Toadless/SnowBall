package uk.toadl3ss.lavalite.agent;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.toadl3ss.lavalite.audio.GuildMusicManager;
import uk.toadl3ss.lavalite.audio.PlayerManager;
import uk.toadl3ss.lavalite.entities.cache.GuildCache;

import java.util.concurrent.atomic.AtomicInteger;

public class VoiceChannelCleanupAgent extends Thread
{
    private static final Logger log = LoggerFactory.getLogger(VoiceChannelCleanupAgent.class);
    private static final int INTERVAL_MILLIS = 60000;
    @Override
    public void run()
    {
        log.info("Started VoiceChannelCleanupAgent");
        while (true)
        {
            try
            {
                cleanup();
                sleep(INTERVAL_MILLIS);
            } catch (Exception e)
            {
                log.error("Caught an exception while trying cleanup voice channels!", e);
                try
                {
                    sleep(1000);
                } catch (InterruptedException e1)
                {
                    log.error("Interrupted while sleeping after an exception in voice channel watchdog", e);
                }
            }
        }
    }
    private static void cleanup()
    {
        GuildCache.cache.forEach(((aLong, guild) ->
        {
            GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
            VoiceChannel vc = guild.getAudioManager().getConnectedChannel();
            if (vc == null)
            {
                return; // If there is no voice channel we dont want to check how many members is in the "voice channel".
            }
            AtomicInteger humansInVC = getHumanMembersInVC(vc);
            if (Integer.parseInt(String.valueOf(humansInVC)) == 0)
            {
                musicManager.audioPlayer.destroy();
                AudioManager audioManager = guild.getAudioManager();
                audioManager.closeAudioConnection();
                log.info("Disconnect from one of " + guild.getName() + "'s voice channels because of inactivity.");
            }
        }));
    }

    private static AtomicInteger getHumanMembersInVC(VoiceChannel vc)
    {
        AtomicInteger members = new AtomicInteger();
        vc.getMembers().forEach(member ->
        {
            if (member.getUser().isBot())
            {
                return;
            }
            members.getAndIncrement();
        });
        return members;
    }
}