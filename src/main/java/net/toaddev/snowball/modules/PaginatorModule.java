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

package net.toaddev.snowball.modules;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.toaddev.snowball.objects.Emoji;
import net.toaddev.snowball.objects.module.Module;
import net.toaddev.snowball.objects.paginator.Paginator;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

public class PaginatorModule extends Module
{
    private Cache<Long, Paginator> paginators;

    public PaginatorModule()
    {
        super("paginator");
    }

    @Override
    public void onEnable()
    {
        this.paginators = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .removalListener((messageId, value, cause) -> remove((Paginator) value))
                .recordStats()
                .build();
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event)
    {
        if(event.getUser().isBot())
        {
            return;
        }
        var paginator = this.paginators.getIfPresent(event.getMessageIdLong());
        if(paginator == null)
        {
            return;
        }
        var code = event.getReactionEmote().getAsReactionCode();
        var currentPage = paginator.getCurrentPage();
        var maxPages = paginator.getMaxPages();

        if(Emoji.ARROW_LEFT.get().equals(code))
        {
            if(currentPage != 0)
            {
                paginator.previousPage();
                event.getChannel().editMessageById(event.getMessageIdLong(), paginator.constructEmbed()).queue();
            }
        }
        else if(Emoji.ARROW_RIGHT.get().equals(code))
        {
            if(currentPage != maxPages - 1)
            {
                paginator.nextPage();
                event.getChannel().editMessageById(event.getMessageIdLong(), paginator.constructEmbed()).queue();
            }
        }
        else if(Emoji.WASTEBASKET.get().equals(code))
        {
            if(paginator.getAuthorId() == event.getUserIdLong())
            {
                try
                {
                    this.paginators.invalidate(event.getMessageIdLong());
                }
                catch (Exception ignored)
                {

                }
                event.getChannel().deleteMessageById(event.getMessageIdLong()).queue();
                return;
            }
        }
        event.getReaction().removeReaction(event.getUser()).queue();
    }

    public void remove(Paginator paginator)
    {
        var guild = this.modules.getGuildById(paginator.getGuildId());
        if(guild == null)
        {
            return;
        }
        var channel = guild.getTextChannelById(paginator.getChannelId());
        if(channel == null)
        {
            return;
        }
        channel.clearReactionsById(paginator.getMessageId()).queue(null, new ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE, (e) ->
        {

        }));
    }

    public void create(TextChannel channel, long authorId, int maxPages, BiFunction<Integer, EmbedBuilder, EmbedBuilder> embedFunction)
    {
        var embedBuilder = embedFunction.apply(0, new EmbedBuilder().setFooter("Page: 1/" + maxPages)).build();
        create(maxPages, embedFunction, embedBuilder, channel, authorId);
    }

    public void create(int maxPages, BiFunction<Integer, EmbedBuilder, EmbedBuilder> embedFunction, MessageEmbed embedBuilder, TextChannel channel, long userId)
    {
        channel.sendMessage(embedBuilder).queue(message ->
        {
            var paginator = new Paginator(message, userId, maxPages, embedFunction);
            if(channel.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION))
            {
                if(maxPages > 1)
                {
                    message.addReaction(Emoji.ARROW_LEFT.get()).queue();
                    message.addReaction(Emoji.ARROW_RIGHT.get()).queue();
                }
                message.addReaction(Emoji.WASTEBASKET.get()).queue();
            }
            this.paginators.put(paginator.getMessageId(), paginator);
        });
    }

    @Override
    public void onDisable()
    {
        this.paginators.cleanUp();
    }
}