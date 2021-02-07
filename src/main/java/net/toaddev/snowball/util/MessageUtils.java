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

package net.toaddev.snowball.util;

import net.dv8tion.jda.api.entities.TextChannel;
import net.toaddev.snowball.entities.command.Command;
import net.toaddev.snowball.modules.PaginatorModule;
import net.toaddev.snowball.services.Modules;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

public class MessageUtils
{
    private static final String UNSET = "unset";

    private MessageUtils()
    {
        // override, default, constructor
    }

    public static String getUserMention(long userId){
        if(userId == -1L){
            return UNSET;
        }
        return "<@!" + userId + ">";
    }

    public static <T> String pluralize(String text, Collection<T> collection){
        return pluralize(text, collection.size());
    }

    public static String pluralize(String text, int count){
        return count == 1 ? text : text + "s";
    }

    public static String trimIfTooLong(String message){
        return message.length() > 2048 ? message.substring(0, 2045) + "..." : message;
    }

    public static String maskLink(String title, String url){
        return "[" + title + "](" + url + ")";
    }

    public static void sendCommands(Collection<Command> commands, Modules modules, TextChannel channel, long authorId, String baseMessage){
        if(channel == null){
            return;
        }
        var cmdMessage = new StringBuilder("**").append(baseMessage).append(":**\n");
        var pages = new ArrayList<String>();

        for(var command : commands){
            var formattedCmd = "`" + command.getName() + "` - `" + command.getDescription() + "`\n";
            if(cmdMessage.length() + formattedCmd.length() >= 516){
                pages.add(cmdMessage.toString());
                cmdMessage = new StringBuilder();
            }
            cmdMessage.append(formattedCmd);
        }
        pages.add(cmdMessage.toString());

        modules.get(PaginatorModule.class).create(
                channel,
                authorId,
                pages.size(),
                (page, embedBuilder) -> embedBuilder.setColor(DiscordUtil.getEmbedColor())
                        .setDescription(pages.get(page))
                        .setTimestamp(Instant.now())
        );
    }
}