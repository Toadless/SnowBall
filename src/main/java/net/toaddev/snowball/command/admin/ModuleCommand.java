package net.toaddev.snowball.command.admin;

import net.toaddev.snowball.data.Config;
import net.toaddev.snowball.main.BotController;
import net.toaddev.snowball.objects.command.Command;
import net.toaddev.snowball.objects.command.CommandContext;
import net.toaddev.snowball.objects.command.CommandFlag;
import net.toaddev.snowball.objects.command.options.CommandOptionString;
import net.toaddev.snowball.objects.exception.CommandException;
import net.toaddev.snowball.objects.module.Module;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@net.toaddev.snowball.annotation.Command
public class ModuleCommand extends Command
{
    public ModuleCommand()
    {
        super("module", "Allows you to modiy the mots modules.");
        addFlags(CommandFlag.DEVELOPER_ONLY);

        addOptions(
                new CommandOptionString("module", "The module you want to modify.").required(),
                new CommandOptionString("action", "What do you want to do with the module?").required()
        );
    }

    @Override
    public void run(@NotNull CommandContext ctx, @NotNull Consumer<CommandException> failure)
    {
        ctx.getEvent().deferReply().queue();

        if (!Config.INS.getDevelopment())
        {
            ctx.getChannel().sendMessage("This command can only be ran in development.").queue();
            return;
        }

        Module m = BotController.getModules().modules.stream().filter(module -> module.getName().equalsIgnoreCase(ctx.getOption("module"))).findFirst().orElse(null);

        if (m == null)
        {
            ctx.getChannel().sendMessage("Please provide a valid module!").queue();
            return;
        }

        boolean completed = true;

        switch (ctx.getOption("action").toLowerCase())
        {
            case "restart", "reload" -> m.reload();
            case "stop", "disable" -> m.onDisable();
            case "start", "enable" -> m.onEnable();

            default -> completed = false;
        }

        if (completed)
        {
            ctx.getChannel().sendMessage("Successfully completed the operation.").queue();
        } else
        {
            ctx.getChannel().sendMessage("Failed to complete the operation.").queue();
        }
    }
}
