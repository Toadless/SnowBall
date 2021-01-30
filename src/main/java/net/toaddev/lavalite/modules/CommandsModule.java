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

package net.toaddev.lavalite.modules;

import net.toaddev.lavalite.command.admin.*;
import net.toaddev.lavalite.command.fun.JokeCommand;
import net.toaddev.lavalite.command.maintenance.ShardsCommand;
import net.toaddev.lavalite.command.maintenance.StatsCommand;
import net.toaddev.lavalite.command.maintenance.VersionCommand;
import net.toaddev.lavalite.command.music.control.*;
import net.toaddev.lavalite.command.music.info.DurationCommand;
import net.toaddev.lavalite.command.music.info.InfoCommand;
import net.toaddev.lavalite.command.music.info.NowPlayingCommand;
import net.toaddev.lavalite.command.music.info.QueueCommand;
import net.toaddev.lavalite.command.music.seeking.RestartCommand;
import net.toaddev.lavalite.command.music.seeking.SeekCommand;
import net.toaddev.lavalite.command.util.*;
import net.toaddev.lavalite.entities.command.Command;
import net.toaddev.lavalite.entities.modules.Module;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandsModule extends Module
{
    private org.slf4j.Logger logger;
    public Map<String, Command> registry;

    public CommandsModule()
    {
        super("commands");
    }

    @Override
    public void onEnable()
    {
        this.registry = new HashMap<>();
        this.logger = LoggerFactory.getLogger(CommandsModule.class);

        initCommands();
    }

    public void initCommands()
    {
        logger.info("Loading all commands... ");
        registerAllCommands();
        logger.info("Registered {} commands correctly!", getSize());
    }

    /**
     *
     * @param command The {@link net.toaddev.lavalite.entities.command.Command command} to register.
     */
    public void registerCommand(Command command)
    {
        logger.info("Registered the command" + " " + command.getName() + ".");
        registry.put(command.getName(), command);
    }

    /**
     *
     * @param command The {@link net.toaddev.lavalite.entities.command.Command command}.
     * @param alias The alias for the {@link net.toaddev.lavalite.entities.command.Command command}.
     */
    public void registerAlias(String command, String alias)
    {
        logger.info("Registered the alias" + " " + alias + ".");
        registry.put(alias, registry.get(command));
    }

    /**
     *
     * @param name The {@link net.toaddev.lavalite.entities.command.Command command} name to find the command by.
     * @return The {@link net.toaddev.lavalite.entities.command.Command command} that has been found.
     */
    public Command getCommand(String name)
    {
        return registry.get(name);
    }

    public int getSize()
    {
        return registry.size();
    }

    public Set<String> getRegisteredCommandsAndAliases()
    {
        return registry.keySet();
    }

    public void registerAllCommands()
    {
        registerCommand(new TestCommand());
        registerCommand(new CommandsCommand());
        registerCommand(new HelpCommand());
        registerCommand(new InviteCommand());
        registerCommand(new VersionCommand());
        registerCommand(new StatsCommand());
        registerCommand(new ShardsCommand());
        registerCommand(new PingCommand());
        registerCommand(new PlayCommand());
        registerCommand(new JoinCommand());
        registerCommand(new LeaveCommand());
        registerCommand(new NowPlayingCommand());
        registerCommand(new QueueCommand());
        registerCommand(new RepeatCommand());
        registerCommand(new SkipCommand());
        registerCommand(new StopCommand());
        registerCommand(new VolumeCommand());
        registerCommand(new ShuffleCommand());
        registerCommand(new DestroyCommand());
        registerCommand(new PauseCommand());
        registerCommand(new RestartCommand());
        registerCommand(new InfoCommand());
        registerCommand(new SeekCommand());
        registerCommand(new EvalCommand());
        registerCommand(new ExitCommand());
        registerCommand(new ReviveCommand());
        registerCommand(new RegistryCommand());
        registerCommand(new PrefixCommand());
        registerCommand(new SoundCloud());
        registerCommand(new DurationCommand());
        registerCommand(new JokeCommand());

        registerAlias("prefix", "setprefix");
        registerAlias("setvolume", "volume");
        registerAlias("skip", "next");
        registerAlias("repeat", "loop");
        registerAlias("nowplaying", "np");
        registerAlias("leave", "disconnect");
        registerAlias("join", "summon");
        registerAlias("join", "connect");
        registerAlias("pause", "resume");
        registerAlias("soundcloud", "sc");
        registerAlias("play", "p");
        registerAlias("duration", "position");
        registerAlias("volume", "vol");
    }

    @Override
    public void onDisable()
    {
        registry.clear();
    }
}