package uk.toadl3ss.lavalite.commandmeta.init;

import uk.toadl3ss.lavalite.command.music.control.*;
import uk.toadl3ss.lavalite.command.music.info.InfoCommand;
import uk.toadl3ss.lavalite.command.music.info.NowPlayingCommand;
import uk.toadl3ss.lavalite.command.music.info.QueueCommand;
import uk.toadl3ss.lavalite.command.music.seeking.RestartCommand;
import uk.toadl3ss.lavalite.command.util.HelpCommand;
import uk.toadl3ss.lavalite.commandmeta.CommandRegistry;
import uk.toadl3ss.lavalite.command.admin.EvalCommand;
import uk.toadl3ss.lavalite.command.admin.ExitCommand;
import uk.toadl3ss.lavalite.command.admin.ReviveCommand;
import uk.toadl3ss.lavalite.command.admin.TestCommand;
import uk.toadl3ss.lavalite.command.maintenance.ShardsCommand;
import uk.toadl3ss.lavalite.command.maintenance.StatsCommand;
import uk.toadl3ss.lavalite.command.maintenance.VersionCommand;
import uk.toadl3ss.lavalite.command.util.CommandsCommand;
import uk.toadl3ss.lavalite.command.util.InviteCommand;
import uk.toadl3ss.lavalite.command.util.PrefixCommand;

public class CommandInitializer {
    // ################################################################################
    // ##                     Initializing Commands
    // ################################################################################
    public static void initCommands() {
        CommandRegistry.registerCommand("test", new TestCommand());
        CommandRegistry.registerCommand("commands", new CommandsCommand());
        CommandRegistry.registerCommand("help", new HelpCommand());
        CommandRegistry.registerCommand("invite", new InviteCommand());
        CommandRegistry.registerCommand("version", new VersionCommand());
        CommandRegistry.registerCommand("stats", new StatsCommand());
        CommandRegistry.registerCommand("shards", new ShardsCommand());
        CommandRegistry.registerCommand("play", new PlayCommand());
        CommandRegistry.registerCommand("join", new JoinCommand());
        CommandRegistry.registerAlias("join", "summon");
        CommandRegistry.registerAlias("join", "connect");
        CommandRegistry.registerCommand("leave", new LeaveCommand());
        CommandRegistry.registerAlias("leave", "disconnect");
        CommandRegistry.registerCommand("nowplaying", new NowPlayingCommand());
        CommandRegistry.registerAlias("nowplaying", "np");
        CommandRegistry.registerCommand("queue", new QueueCommand());
        CommandRegistry.registerCommand("repeat", new RepeatCommand());
        CommandRegistry.registerAlias("repeat", "loop");
        CommandRegistry.registerCommand("skip", new SkipCommand());
        CommandRegistry.registerAlias("skip", "next");
        CommandRegistry.registerCommand("stop", new StopCommand());
        CommandRegistry.registerCommand("setvolume", new VolumeCommand());
        CommandRegistry.registerAlias("setvolume", "volume");
        CommandRegistry.registerCommand("shuffle", new ShuffleCommand());
        CommandRegistry.registerCommand("destroy", new DestroyCommand());
        CommandRegistry.registerCommand("pause", new PauseCommand());
        CommandRegistry.registerCommand("restart", new RestartCommand());
        CommandRegistry.registerCommand("info", new InfoCommand());

        CommandRegistry.registerCommand("eval", new EvalCommand());
        CommandRegistry.registerCommand("exit", new ExitCommand());
        CommandRegistry.registerCommand("revive", new ReviveCommand());

        CommandRegistry.registerCommand("prefix", new PrefixCommand());
        CommandRegistry.registerAlias("prefix", "setprefix");
    }
}