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

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.toaddev.snowball.entities.command.Command;
import net.toaddev.snowball.entities.command.CommandContext;
import net.toaddev.snowball.entities.module.Module;
import net.toaddev.snowball.main.BotController;

public class InteractionsModule extends Module
{
    @Override
    public void onEnable()
    {

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        String mention = "<@!" + event.getJDA().getSelfUser().getId() + ">";
        long guildId = Long.parseLong(event.getGuild().getId());

        if (event.getMessage().isWebhookMessage())
        {
            return;
        }
        else if (event.getAuthor().equals(event.getJDA().getSelfUser()))
        {
            return;
        }
        else if (event.getMember().getUser().isBot())
        {
            return;
        }
        String guildPrefix = BotController.getModules().get(SettingsModule.class).getGuildPrefix(guildId);
        if (event.getMessage().getContentRaw().startsWith(guildPrefix))
        {
            this.process(event, guildPrefix);
        }
        else if (event.getMessage().getContentRaw().startsWith(mention))
        {
            this.process(event, guildPrefix);
        }
    }

    public void process(GuildMessageReceivedEvent event, String prefix)
    {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        Command command = BotController.getModules().get(CommandsModule.class).getCommand(args[0].replace(prefix, ""));

        if (command == null)
        {
            return;
        }

        CommandContext commandEvent = new CommandContext(event, args, prefix);
        command.process(commandEvent);
    }
}