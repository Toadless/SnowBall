package net.toaddev.lavalite.entities.command.init;

import net.toaddev.lavalite.command.admin.*;
import net.toaddev.lavalite.command.music.control.*;
import net.toaddev.lavalite.command.music.info.DurationCommand;
import net.toaddev.lavalite.command.music.info.InfoCommand;
import net.toaddev.lavalite.command.music.seeking.SeekCommand;
import net.toaddev.lavalite.command.util.*;
import net.toaddev.lavalite.entities.command.CommandRegistry;
import uk.toadl3ss.lavalite.command.admin.*;
import net.toaddev.lavalite.command.fun.JokeCommand;
import uk.toadl3ss.lavalite.command.music.control.*;
import net.toaddev.lavalite.command.music.info.NowPlayingCommand;
import net.toaddev.lavalite.command.music.info.QueueCommand;
import net.toaddev.lavalite.command.music.seeking.RestartCommand;
import uk.toadl3ss.lavalite.command.util.*;
import net.toaddev.lavalite.command.maintenance.ShardsCommand;
import net.toaddev.lavalite.command.maintenance.StatsCommand;
import net.toaddev.lavalite.command.maintenance.VersionCommand;

public class CommandInitializer
{
    // ################################################################################
    // ##                     Initializing Commands
    // ################################################################################
    public static void initCommands()
    {
        /* Registering commands */
        CommandRegistry.registerCommand(new TestCommand());
        CommandRegistry.registerCommand(new CommandsCommand());
        CommandRegistry.registerCommand(new HelpCommand());
        CommandRegistry.registerCommand(new InviteCommand());
        CommandRegistry.registerCommand(new VersionCommand());
        CommandRegistry.registerCommand(new StatsCommand());
        CommandRegistry.registerCommand(new ShardsCommand());
        CommandRegistry.registerCommand(new PingCommand());
        CommandRegistry.registerCommand(new PlayCommand());
        CommandRegistry.registerCommand(new JoinCommand());
        CommandRegistry.registerCommand(new LeaveCommand());
        CommandRegistry.registerCommand(new NowPlayingCommand());
        CommandRegistry.registerCommand(new QueueCommand());
        CommandRegistry.registerCommand(new RepeatCommand());
        CommandRegistry.registerCommand(new SkipCommand());
        CommandRegistry.registerCommand(new StopCommand());
        CommandRegistry.registerCommand(new VolumeCommand());
        CommandRegistry.registerCommand(new ShuffleCommand());
        CommandRegistry.registerCommand(new DestroyCommand());
        CommandRegistry.registerCommand(new PauseCommand());
        CommandRegistry.registerCommand(new RestartCommand());
        CommandRegistry.registerCommand(new InfoCommand());
        CommandRegistry.registerCommand(new SeekCommand());
        CommandRegistry.registerCommand(new EvalCommand());
        CommandRegistry.registerCommand(new ExitCommand());
        CommandRegistry.registerCommand(new ReviveCommand());
        CommandRegistry.registerCommand(new RegistryCommand());
        CommandRegistry.registerCommand(new PrefixCommand());
        CommandRegistry.registerCommand(new SoundCloud());
        CommandRegistry.registerCommand(new DurationCommand());
        CommandRegistry.registerCommand(new JokeCommand());

        /* Registering every alias */
        CommandRegistry.registerAlias("prefix", "setprefix");
        CommandRegistry.registerAlias("setvolume", "volume");
        CommandRegistry.registerAlias("skip", "next");
        CommandRegistry.registerAlias("repeat", "loop");
        CommandRegistry.registerAlias("nowplaying", "np");
        CommandRegistry.registerAlias("leave", "disconnect");
        CommandRegistry.registerAlias("join", "summon");
        CommandRegistry.registerAlias("join", "connect");
        CommandRegistry.registerAlias("pause", "resume");
        CommandRegistry.registerAlias("soundcloud", "sc");
        CommandRegistry.registerAlias("play", "p");
        CommandRegistry.registerAlias("duration", "position");
        CommandRegistry.registerAlias("volume", "vol");
    }
}