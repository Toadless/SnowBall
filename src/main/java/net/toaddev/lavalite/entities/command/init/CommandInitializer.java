package net.toaddev.lavalite.entities.command.init;

import net.toaddev.lavalite.command.admin.*;
import net.toaddev.lavalite.command.music.control.*;
import net.toaddev.lavalite.command.music.info.DurationCommand;
import net.toaddev.lavalite.command.music.info.InfoCommand;
import net.toaddev.lavalite.command.music.seeking.SeekCommand;
import net.toaddev.lavalite.command.util.*;
import net.toaddev.lavalite.entities.command.CommandRegistry;
import net.toaddev.lavalite.command.fun.JokeCommand;
import net.toaddev.lavalite.command.music.info.NowPlayingCommand;
import net.toaddev.lavalite.command.music.info.QueueCommand;
import net.toaddev.lavalite.command.music.seeking.RestartCommand;
import net.toaddev.lavalite.command.maintenance.ShardsCommand;
import net.toaddev.lavalite.command.maintenance.StatsCommand;
import net.toaddev.lavalite.command.maintenance.VersionCommand;

import static net.toaddev.lavalite.entities.command.CommandRegistry.registerAlias;
import static net.toaddev.lavalite.entities.command.CommandRegistry.registerCommand;

public class CommandInitializer
{
    public static void initCommands()
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
}