package uk.toadl3ss.lavalite.commandmeta.init;

import uk.toadl3ss.lavalite.commandmeta.CommandRegistry;
import uk.toadl3ss.lavalite.commands.admin.EvalCommand;
import uk.toadl3ss.lavalite.commands.admin.ExitCommand;
import uk.toadl3ss.lavalite.commands.admin.ReviveCommand;
import uk.toadl3ss.lavalite.commands.maintenance.ShardsCommand;
import uk.toadl3ss.lavalite.commands.maintenance.StatsCommand;
import uk.toadl3ss.lavalite.commands.maintenance.VersionCommand;
import uk.toadl3ss.lavalite.commands.music.*;
import uk.toadl3ss.lavalite.commands.util.HelpCommand;
import uk.toadl3ss.lavalite.commands.util.InviteCommand;

public class CommandInitializer {
    // ################################################################################
    // ##                     Initializing Commands
    // ################################################################################
    public static void initCommands() {

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
        CommandRegistry.registerCommand("queue", new NowPlayingCommand());
        CommandRegistry.registerCommand("repeat", new RepeatCommand());
        CommandRegistry.registerAlias("repeat", "loop");
        CommandRegistry.registerCommand("skip", new SkipCommand());
        CommandRegistry.registerAlias("skip", "next");
        CommandRegistry.registerCommand("stop", new StopCommand());

        CommandRegistry.registerCommand("eval", new EvalCommand());
        CommandRegistry.registerCommand("exit", new ExitCommand());
        CommandRegistry.registerCommand("revive", new ReviveCommand());
    }
}