/*
 * MIT License
 *
 * Copyright (c) 2021 Toadless @ toaddev.net
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

package net.toaddev.lavalite.modules;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import net.toaddev.lavalite.audio.GuildMusicManager;
import net.toaddev.lavalite.data.Config;
import net.toaddev.lavalite.entities.command.CommandContext;
import net.toaddev.lavalite.entities.module.Module;
import net.toaddev.lavalite.entities.music.SearchProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
@SuppressWarnings("unused")
public class SpotifyModule extends Module
{
    private static final Logger LOG = LoggerFactory.getLogger(SpotifyModule.class);

    private SpotifyApi spotify;
    private ClientCredentialsRequest clientCredentialsRequest;
    private int hits;

    @Override
    public void onEnable()
    {
        this.spotify = new SpotifyApi.Builder().setClientId(Config.INS.getSpotifyId()).setClientSecret(Config.INS.getSpotifyApiKey()).build();
        this.clientCredentialsRequest = this.spotify.clientCredentials().build();
        this.modules.scheduleAtFixedRate(this::refreshAccessToken, 0, 1, TimeUnit.HOURS);
        this.hits = 0;
    }

    private void refreshAccessToken()
    {
        try
        {
            this.spotify.setAccessToken(this.clientCredentialsRequest.execute().getAccessToken());
            this.hits = 0;
        }
        catch(Exception e)
        {
            this.hits++;
            if(this.hits < 10)
            {
                LOG.warn("Updating the access token failed. Retrying in 10 seconds", e);
                this.modules.schedule(this::refreshAccessToken, 10, TimeUnit.SECONDS);
                return;
            }
            LOG.error("Updating the access token failed. Retrying in 20 seconds", e);
            this.modules.schedule(this::refreshAccessToken, 20, TimeUnit.SECONDS);
        }
    }

    public void load(CommandContext ctx, MusicModule manager, Matcher matcher)
    {
        switch(matcher.group(3))
        {
            case "album":
                loadAlbum(matcher.group(4), ctx, manager);
                break;
            case "track":
                loadTrack(matcher.group(4), ctx, manager);
                break;
            case "playlist":
                loadPlaylist(matcher.group(4), ctx, manager);
                break;
        }
    }

    private void loadAlbum(String id, CommandContext ctx, MusicModule manager)
    {
        this.spotify.getAlbumsTracks(id).build().executeAsync().thenAcceptAsync(tracks ->
        {
            var items = tracks.getItems();
            var toLoad = new ArrayList<String>();
            for(var track : items)
            {
                toLoad.add("ytsearch:" + track.getArtists()[0].getName() + " " + track.getName());
            }
            loadTracks(id, ctx, manager, toLoad, manager.getMusicManager(ctx.getGuild()));
        }).exceptionally(throwable ->
        {
            ctx.getChannel().sendMessage(throwable.getMessage().contains("invalid id") ? "Album not found" : "There was an error while loading the album").queue();
            return null;
        });
    }

    private void loadTrack(String id, CommandContext ctx, MusicModule manager)
    {
        this.spotify.getTrack(id).build().executeAsync().thenAcceptAsync(track -> this.modules.get(MusicModule.class).loadAndPlay(ctx.getChannel(), track.getArtists()[0].getName() + " " + track.getName(), SearchProvider.YOUTUBE, ctx, false, false))
                .exceptionally(throwable ->
                {
                    ctx.getChannel().sendMessage(throwable.getMessage().contains("invalid id") ? "Track not found" : "There was an error while loading the track").queue();
                    return null;
                });
    }

    private void loadPlaylist(String id, CommandContext ctx, MusicModule manager)
    {
        this.spotify.getPlaylistsItems(id).build().executeAsync().thenAcceptAsync(tracks ->
        {
            var items = tracks.getItems();
            var toLoad = new ArrayList<String>();
            for(var item : items){
                var track = (Track) item.getTrack();
                toLoad.add("ytsearch:" + track.getArtists()[0].getName() + " " + track.getName());
            }
            loadTracks(id, ctx, manager, toLoad, manager.getMusicManager(ctx.getGuild()));
        }).exceptionally(throwable ->
        {
            ctx.getChannel().sendMessage(throwable.getMessage().contains("Invalid playlist Id") ? "Playlist not found" : "There was an error while loading the playlist").queue();
            return null;
        });
    }

    private void loadTracks(String id, CommandContext ctx, MusicModule manager, List<String> toLoad, GuildMusicManager guildMusicManager)
    {
        ctx.getChannel().sendMessage("Attempting to load `" + toLoad.size() + "` tracks to the queue from spotify!").queue();
        toLoad.forEach(s ->
        {
            manager.loadAndPlay(ctx.getChannel(), s, SearchProvider.YOUTUBE, ctx, false, false);
        });
    }
}