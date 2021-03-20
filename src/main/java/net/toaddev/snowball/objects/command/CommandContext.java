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

package net.toaddev.snowball.objects.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.toaddev.snowball.main.BotController;
import net.toaddev.snowball.services.Modules;
import net.toaddev.snowball.util.DiscordUtil;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A class representing an event for a {@link Command command}.
 */
public class CommandContext
{
    private final SlashCommandEvent event;
    private final JDA jda;
    private final MessageChannel channel;
    private final Member member;
    private final Guild guild;

    public CommandContext(SlashCommandEvent event)
    {
        this.event = event;
        this.jda = event.getJDA();
        this.channel = event.getChannel();
        this.member = event.getMember();
        this.guild = event.getGuild();
    }

    public SlashCommandEvent getEvent()
    {
        return event;
    }

    /**
     * @return The {@link net.dv8tion.jda.api.JDA jda instance}.
     */
    public JDA getJDA()
    {
        return jda;
    }

    /**
     * @return The {@link net.dv8tion.jda.api.entities.Guild guild} the event took place in.
     */
    public Guild getGuild()
    {
        return guild;
    }

    /**
     * @return The {@link net.dv8tion.jda.api.entities.Member member} for this {@link CommandContext event}.
     * @throws java.lang.NullPointerException if the member is null.
     */
    public Member getMember()
    {
        return member;
    }

    /**
     * @return The channel that the event took place in
     */
    public MessageChannel getChannel()
    {
        return channel;
    }

    /**
     * @return If the {@link #getMember()} is a developer.
     */
    public Boolean isDeveloper()
    {
        return DiscordUtil.isOwner(getMember().getUser());
    }

    /**
     * @return If the {@link #getMember()} is a server admin.
     */
    public Boolean isServerAdmin()
    {
        return DiscordUtil.isServerAdmin(getMember());
    }

    /**
     * Checks whether the {@link net.dv8tion.jda.api.entities.Member self member} has all the needed permissions for execution.
     *
     * @param permissions The {@link net.dv8tion.jda.api.Permission permissions} to check.
     * @return Whether the {@link net.dv8tion.jda.api.entities.Member author} has the requred permissions.
     */
    @Nonnull
    public Boolean selfPermissionCheck(List<Permission> permissions)
    {
        return event.getGuild().getSelfMember().hasPermission(permissions);
    }

    /**
     * Checks whether the author has all the needed permissions for execution.
     *
     * @param permissions The {@link net.dv8tion.jda.api.Permission permissions} to check.
     * @return Whether the {@link net.dv8tion.jda.api.entities.Member author} has the {@link Command#getMemberRequiredPermissions() permissions}.
     */
    @Nonnull
    public Boolean memberPermissionCheck(List<Permission> permissions)
    {
        return (event.getMember() != null && event.getMember().hasPermission((GuildChannel) event.getChannel(), permissions));
    }

    public Modules getModules()
    {
        return BotController.getModules();
    }

    public void sendMessage(MessageEmbed content)
    {
        this.channel.sendMessage(content).queue();
    }

    public String getOption(String option)
    {
        return event.getOption(option).getAsString();
    }

    public boolean isDefined(String option)
    {
        return !(event.getOption(option) == null);
    }
}