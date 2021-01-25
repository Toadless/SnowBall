package uk.toadl3ss.lavalite.entities.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.LoggerFactory;
import uk.toadl3ss.lavalite.entities.command.abs.Command;
import uk.toadl3ss.lavalite.entities.command.abs.ICommandMusic;
import uk.toadl3ss.lavalite.entities.exception.CommandException;
import uk.toadl3ss.lavalite.entities.exception.CommandFlagException;
import uk.toadl3ss.lavalite.util.DiscordUtil;

import java.util.ArrayList;

public class CommandManager
{
    // ################################################################################
    // ##                     Command Manager
    // ################################################################################
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommandManager.class);
    public static void executeCommand(String args[], GuildMessageReceivedEvent event, String prefix)
    {
        String command = args[0].replaceFirst("^" + prefix, "");
        Command cmd = CommandRegistry.getCommand(command);
        if (cmd == null)
        {
            return;
        }

        if (cmd.getFlag() == null) {
            event.getChannel().sendMessage("You cannot run this command since it has not been flagged.").queue();
            throw new CommandFlagException("The command: " + cmd.getName() + " has not been flagged.");
        }

        // Check command type
        if (cmd.getFlag().equals(CommandFlags.DEVELOPER_ONLY))
        {
            if (!DiscordUtil.isOwner(event.getMember().getUser()))
            {
                event.getChannel().sendMessage("You cannot run a dev command since you dont have permission.").queue();
                throw new CommandException("No permission for a dev command.");
            }
        }
        if (cmd.getFlag().equals(CommandFlags.SERVER_ADMIN_ONLY))
        {
            if (!DiscordUtil.isServerAdmin(event.getMember()))
            {
                event.getChannel().sendMessage("You do not have to required permission to perform this action.").queue();
                return;
            }
        }
        if (cmd.getFlag().equals(CommandFlags.DISABLED)) {
            event.getChannel().sendMessage("You cannot run this command since it has been disabled.").queue();
            throw new CommandException("This command is disabled.");
        }

        // Music perm checks
        if (cmd instanceof ICommandMusic)
        {
            Member member = event.getGuild().getSelfMember();
            if (!member.hasPermission(Permission.VOICE_CONNECT))
            {
                event.getChannel().sendMessage("Cannot join voice channel. Error: `Missing permissions`.").queue();
                return;
            }
            if (!member.hasPermission(Permission.VOICE_SPEAK))
            {
                event.getChannel().sendMessage("Will not join channel with out permission: `VOICE_SPEAK`!").queue();
                return;
            }
        }

        String[] arguments = commandToArguments(event.getMessage().getContentRaw());
        CommandEvent ctx = new CommandEvent(event, arguments, prefix);

        cmd.run(ctx);
    }

    private static String[] commandToArguments(String cmd)
    {
        ArrayList<String> a = new ArrayList<>();
        int argi = 0;
        boolean isInQuote = false;

        for (Character ch : cmd.toCharArray()) {
            if (Character.isWhitespace(ch) && !isInQuote)
            {
                String arg = null;
                try {
                    arg = a.get(argi);
                } catch (IndexOutOfBoundsException e)
                {
                }
                if (arg != null)
                {
                    argi++;
                }

            } else if (ch.equals('"'))
            {
                isInQuote = !isInQuote;
            } else {
                a = writeToArg(a, argi, ch);
            }
        }

        String[] newA = new String[a.size()];
        int i = 0;
        for (String str : a) {
            newA[i] = str;
            i++;
        }

        return newA;
    }

    private static ArrayList<String> writeToArg(ArrayList<String> a, int argi, char ch)
    {
        String arg = null;
        try {
            arg = a.get(argi);
        } catch (IndexOutOfBoundsException ignored)
        {
        }
        if (arg == null) {
            a.add(argi, String.valueOf(ch));
        } else {
            a.set(argi, arg + ch);
        }

        return a;
    }
}