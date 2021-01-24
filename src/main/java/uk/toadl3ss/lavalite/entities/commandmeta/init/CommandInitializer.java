package uk.toadl3ss.lavalite.entities.commandmeta.init;

import uk.toadl3ss.lavalite.command.admin.*;
import uk.toadl3ss.lavalite.command.music.control.*;
import uk.toadl3ss.lavalite.command.music.info.InfoCommand;
import uk.toadl3ss.lavalite.command.music.info.NowPlayingCommand;
import uk.toadl3ss.lavalite.command.music.info.QueueCommand;
import uk.toadl3ss.lavalite.command.music.seeking.RestartCommand;
import uk.toadl3ss.lavalite.command.music.seeking.SeekCommand;
import uk.toadl3ss.lavalite.command.util.*;
import uk.toadl3ss.lavalite.entities.commandmeta.CommandRegistry;
import uk.toadl3ss.lavalite.command.maintenance.ShardsCommand;
import uk.toadl3ss.lavalite.command.maintenance.StatsCommand;
import uk.toadl3ss.lavalite.command.maintenance.VersionCommand;

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
    }
}