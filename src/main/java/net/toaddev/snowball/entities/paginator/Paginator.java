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

package net.toaddev.snowball.entities.paginator;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.function.BiFunction;

public class Paginator
{
    private final long guildId, channelId, messageId, authorId;
    private final int maxPages;
    private final BiFunction<Integer, EmbedBuilder, EmbedBuilder> embedFunction;
    private int currentPage;

    public Paginator(Message message, long authorId, int maxPages, BiFunction<Integer, EmbedBuilder, EmbedBuilder> embedFunction)
    {
        this.guildId = message.getGuild().getIdLong();
        this.channelId = message.getChannel().getIdLong();
        this.messageId = message.getIdLong();
        this.authorId = authorId;
        this.currentPage = 0;
        this.maxPages = maxPages;
        this.embedFunction = embedFunction;
    }

    public long getGuildId()
    {
        return this.guildId;
    }

    public long getChannelId()
    {
        return this.channelId;
    }

    public long getMessageId()
    {
        return this.messageId;
    }

    public long getAuthorId()
    {
        return this.authorId;
    }

    public int getCurrentPage()
    {
        return this.currentPage;
    }

    public int getMaxPages()
    {
        return this.maxPages;
    }

    public void previousPage()
    {
        this.currentPage--;
    }

    public void nextPage()
    {
        this.currentPage++;
    }

    public MessageEmbed constructEmbed(){
        return this.embedFunction.apply(this.currentPage, new EmbedBuilder().setFooter("Page " + (this.currentPage + 1) + "/" + this.maxPages)).build();
    }
}