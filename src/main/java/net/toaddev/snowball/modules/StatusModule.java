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

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.toaddev.snowball.objects.module.Module;
import net.toaddev.snowball.main.BotController;
import net.toaddev.snowball.util.IOUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.*;

public class StatusModule extends Module
{
    private List<String> statusMessages;

    public StatusModule()
    {
        super("status");
    }

    @Override
    public void onEnable()
    {
        this.statusMessages = IOUtil.loadMessageFile("status");
    }

    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        this.modules.scheduleAtFixedRate(this::newRandomStatus, 0, 2, TimeUnit.MINUTES);
    }

    public void newRandomStatus()
    {
        var JDA = BotController.getJda();
        JDA.getPresence().setPresence(OnlineStatus.ONLINE, generateRandomMessage());
    }

    private Activity generateRandomMessage()
    {
        if(statusMessages.isEmpty())
        {
            return Activity.watching("you \uD83D\uDC40");
        }
        var randomMessage = statusMessages.get(ThreadLocalRandom.current().nextInt(statusMessages.size() - 1));

        var activityMessage = randomMessage.split("\\s+", 2);
        var type = activityMessage[0].toUpperCase();
        var message = activityMessage[1];

        message = message.replace("${total_users}", String.valueOf(BotController.getAllUsersAsMap().size()));
        message = message.replace("${total_guilds}", String.valueOf(BotController.getAllGuilds().size()));
        return Activity.of(Activity.ActivityType.valueOf(type.equals("PLAYING") ? "DEFAULT" : type), message);
    }

    @Override
    public void onDisable()
    {

    }
}