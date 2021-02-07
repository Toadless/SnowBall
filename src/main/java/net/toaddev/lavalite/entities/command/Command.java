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

package net.toaddev.lavalite.entities.command;

import com.mongodb.lang.NonNull;
import net.dv8tion.jda.api.Permission;
import net.toaddev.lavalite.util.EmbedUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a command for use in the {@link net.toaddev.lavalite.modules.CommandsModule CommandManager}.
 *
 */
public abstract class Command
{
    private final String name;
    private final String help;
    /**
     * The {@link net.dv8tion.jda.api.Permission permissions} required by the {@link net.dv8tion.jda.api.entities.Member author} to execute this {@link Command command}.
     */
    private final List<Permission> memberRequiredPermissions;
    /**
     * The {@link net.dv8tion.jda.api.Permission permissions} required by the {@link net.dv8tion.jda.api.entities.Member self member} to execute this {@link Command command}.
     */
    private final List<Permission> selfRequiredPermissions;
    private final List<CommandFlag> flags;
    private final List<String> alias;

    /**
     *
     * @param name The commands name
     * @param help The commands help page
     */
    public Command(@NonNull String name, @NonNull String help)
    {
        this.name = name;
        this.help = help;
        this.memberRequiredPermissions = new ArrayList<>();
        this.selfRequiredPermissions = new ArrayList<>();
        this.flags = new ArrayList<>();
        this.alias = new ArrayList<>();
    }

    /**
     * Processes this {@link net.toaddev.lavalite.modules.CommandsModule command} for execution.
     * <p>
     * This will consider the {@link CommandFlag flags} of this {@link net.toaddev.lavalite.modules.CommandsModule command}
     * <p>
     * This will only {@link #run(CommandContext) run} the command if all checks pass.
     * @param event The command event to process with.
     */
    public void process(CommandContext event)
    {
        if (hasFlag(CommandFlag.DISABLED))
        {
            EmbedUtil.sendDisabledError(event);
        }
        else if (hasFlag(CommandFlag.DEVELOPER_ONLY) && !event.isDeveloper())
        {
            EmbedUtil.sendError(event.getChannel(), "This command is in developer only mode.");
        }
        else if(!getMemberRequiredPermissions().isEmpty() && !event.memberPermissionCheck(getMemberRequiredPermissions()))
        {
            EmbedUtil.sendError(event.getChannel(), "You do not have the required permission to perform this action.");
        }
        else if(!getSelfRequiredPermissions().isEmpty() && !event.selfPermissionCheck(getSelfRequiredPermissions()))
        {
            EmbedUtil.sendError(event.getChannel(), "I do not have the required permission to perform this action.");
        }
        else if (hasFlag(CommandFlag.SERVER_ADMIN_ONLY) && !event.isDeveloper() || hasFlag(CommandFlag.SERVER_ADMIN_ONLY) && !event.isServerAdmin())
        {
            EmbedUtil.sendError(event.getChannel(), "You do not have sufficient permissions to perform this action!");
        }
        else
        {
            if (hasFlag(CommandFlag.AUTO_DELETE_MESSAGE))
            {
                event.getMessage().delete().queue();
            }

            try {
                run(event);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
                EmbedUtil.sendError(event.getChannel(), "An error occurred whilst executing this command. This is most likely a self permission exception.");
            }
        }
    }

    /**
     * @return The {@link CommandFlag flags} for this {@link net.toaddev.lavalite.modules.CommandsModule command}.
     * @see #addFlags(CommandFlag...)
     * @see #hasFlag(CommandFlag)
     */
    @Nonnull
    public List<CommandFlag> getFlags()
    {
        return flags;
    }

    /**
     * Adds {@link CommandFlag flags} to this {@link net.toaddev.lavalite.modules.CommandsModule command}.
     *
     * @param flags The flags to add.
     * @see #getFlags()
     * @see #hasFlag(CommandFlag)
     */
    public void addFlags(@Nonnull CommandFlag... flags)
    {
        this.flags.addAll(List.of(flags));
    }

    /**
     * @param flag The flag to check for.
     * @return Whether this {@link net.toaddev.lavalite.modules.CommandsModule command} has the flag.
     * @see #getFlags()
     * @see #addFlags(CommandFlag...)
     */
    public boolean hasFlag(@Nonnull CommandFlag flag)
    {
        return this.flags.contains(flag);
    }

    /**
     *
     * @param ctx The {@link CommandContext event} to use.
     */
    public abstract void run(@Nonnull CommandContext ctx);

    /**
     * Gets the {@link net.dv8tion.jda.api.Permission permissions} required by the {@link net.toaddev.lavalite.modules.CommandsModule command} {@link net.dv8tion.jda.api.entities.Member author} to execute.
     *
     * @return The permissions.
     * @see #addMemberPermissions(net.dv8tion.jda.api.Permission...)
     */
    @Nonnull
    public List<Permission> getMemberRequiredPermissions()
    {
        return memberRequiredPermissions;
    }

    /**
     * Adds {@link net.dv8tion.jda.api.Permission permissions} required by the {@link net.toaddev.lavalite.modules.CommandsModule command} {@link net.dv8tion.jda.api.entities.Member author} to execute.
     *
     * @param permissions The permissions to add.
     * @see #getMemberRequiredPermissions()
     */
    public void addMemberPermissions(@Nonnull Permission... permissions)
    {
        this.memberRequiredPermissions.addAll(List.of(permissions));
    }


    /**
     * Adds {@link net.dv8tion.jda.api.Permission permissions} required by the {@link net.dv8tion.jda.api.entities.Member self user} to execute the {@link net.toaddev.lavalite.modules.CommandsModule command}.
     *
     * @param permissions The permissions to add.
     * @see #getSelfRequiredPermissions()
     */
    public void addSelfPermissions(@Nonnull Permission... permissions)
    {
        this.selfRequiredPermissions.addAll(List.of(permissions));
    }

    /**
     * Gets the {@link net.dv8tion.jda.api.Permission permissions} required by the {@link net.dv8tion.jda.api.entities.Member self user} to execute the {@link net.toaddev.lavalite.modules.CommandsModule command}.
     *
     * @see #getSelfRequiredPermissions()
     * @return
     */
    public List<Permission> getSelfRequiredPermissions()
    {
        return selfRequiredPermissions;
    }

    public void addAlias(@Nonnull String... alias)
    {
        this.alias.addAll(List.of(alias));
    }

    public List<String> getAliases()
    {
        return this.alias;
    }

    /**
     *
     * @return The command name
     */
    @NonNull
    public String getName()
    {
        return name;
    }

    /**
     *
     * @return Returns the string for the help page
     */
    public String getDescription()
    {
        return help;
    }
}