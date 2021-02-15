package net.toaddev.snowball.command.admin;

import net.toaddev.snowball.data.Config;
import net.toaddev.snowball.entities.command.Command;
import net.toaddev.snowball.entities.command.CommandContext;
import net.toaddev.snowball.entities.command.CommandFlag;
import net.toaddev.snowball.entities.module.Module;
import net.toaddev.snowball.main.BotController;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

@net.toaddev.snowball.annotation.Command
public class ModuleCommand extends Command
{
    public ModuleCommand()
    {
        super("module", null);
        addFlags(CommandFlag.DEVELOPER_ONLY);
    }

    @Override
    public void run(@NotNull CommandContext ctx)
    {
        if (!Config.INS.getDevelopment())
        {
            ctx.getChannel().sendMessage("This command can only be ran in development.").queue();
            return;
        }

        if (ctx.getArgs().length < 2)
        {
            ctx.getChannel().sendMessage("I need to know what to do with the selected module!").queue();
            return;
        }

        if (ctx.getArgs().length < 3)
        {
            ctx.getChannel().sendMessage("Please provide a valid module!").queue();
            return;
        }

        Module m = BotController.getModules().modules.stream().filter(module -> module.getName().equalsIgnoreCase(ctx.getArgs()[2])).findFirst().orElse(null);

        if (m == null)
        {
            ctx.getChannel().sendMessage("Please provide a valid module!").queue();
            return;
        }

        boolean completed = true;

        switch (ctx.getArgs()[1].toLowerCase())
        {
            case "restart" -> m.reload();
            case "stop" -> m.onDisable();
            case "start" -> m.onEnable();
            case "reload" -> m.reload();
            case "disable" -> m.onDisable();
            case "enable" -> m.onEnable();

            default -> completed = false;
        }

        if (completed)
        {
            ctx.getChannel().sendMessage("Successfully completed the operation.").queue();
        }
        else
        {
            ctx.getChannel().sendMessage("Failed to complete the operation.").queue();
        }
    }
}
