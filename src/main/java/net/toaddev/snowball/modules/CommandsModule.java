/*
 *  Copyright (c) 2021 Toadless @ toaddev.net
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package net.toaddev.snowball.modules;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.internal.requests.Method;
import net.dv8tion.jda.internal.requests.Requester;
import net.dv8tion.jda.internal.requests.Route;
import net.toaddev.snowball.annotation.Ignore;
import net.toaddev.snowball.data.Config;
import net.toaddev.snowball.objects.command.Command;
import net.toaddev.snowball.objects.module.Module;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandsModule extends Module
{
    public static final Route COMMANDS_CREATE = Route.custom(Method.PUT, "applications/{application.id}/commands");
    public static final Route GUILD_COMMANDS_CREATE = Route.custom(Method.PUT, "applications/{application.id}/guilds/{guild.id}/commands");
    private static final Logger LOG = LoggerFactory.getLogger(CommandsModule.class);
    private static final String COMMANDS_PACKAGE = "net.toaddev.snowball.command";
    private ClassGraph classGraph;
    private Map<String, Command> commands;

    public CommandsModule()
    {
        super("commands");
    }

    @Override
    public void onEnable()
    {
        this.classGraph = new ClassGraph().acceptPackages(COMMANDS_PACKAGE);
        this.commands = new HashMap<>();

        this.classGraph.enableAnnotationInfo();

        scanCommands();
        deployAllCommands(Config.INS.getSlashCommandId());
    }

    public void scanCommands()
    {
        LOG.info("Loading commands...");

        try (ScanResult result = classGraph.scan())
        {
            for (ClassInfo clazz : result.getAllClasses())
            {
                if (!clazz.hasAnnotation(net.toaddev.snowball.annotation.Command.class.getName()))
                {
                    LOG.warn("Non Command class (" + clazz.getSimpleName() + ") found in commands package!");
                    continue;
                }

                if (clazz.hasAnnotation(Ignore.class.getName()))
                {
                    continue;
                }
                Constructor<?>[] constructors = clazz.loadClass().getDeclaredConstructors();
                if (constructors.length == 0)
                {
                    LOG.warn("No constructor found for Command class (" + clazz.getSimpleName() + ")!");
                    continue;
                }
                if (constructors[0].getParameterCount() > 0)
                {
                    continue;
                }
                Object instance = constructors[0].newInstance();
                if (!(instance instanceof Command))
                {
                    LOG.warn("Non Command class (" + clazz.getSimpleName() + ") found in commands package!");
                    continue;
                }
                Command cmd = (Command) instance;
                commands.put(cmd.getName(), cmd);
            }

            LOG.info("Loaded {} commands", this.commands.size());
        } catch (Exception exception)
        {
            LOG.error("A exception occurred whilst loading commands. Exception: ", exception);
        }
    }

    public void deployAllCommands(long guildId)
    {
        LOG.info("Registering commands {}...", guildId == -1 ? "global" : "for guild " + guildId);

        var commands = DataArray.fromCollection(this.commands.values().stream().filter(command ->
        {
            return command.getDescription() != null;
        }).map(Command::toJSON).collect(Collectors.toList()));
        var rqBody = RequestBody.create(commands.toJson(), MediaType.parse("application/json"));

        var route = guildId == -1L ? COMMANDS_CREATE.compile(String.valueOf(Config.INS.getId())) : GUILD_COMMANDS_CREATE.compile(String.valueOf(Config.INS.getId()), String.valueOf(guildId));
        try (var resp = newCall(route, rqBody).execute())
        {
            if (!resp.isSuccessful())
            {
                var body = resp.body();
                LOG.error("Registering commands failed. Request Body: {}, Response Body: {}", commands.toString(), body == null ? "null" : body.string());
                return;
            }
        } catch (IOException e)
        {
            LOG.error("Error while processing registerCommands", e);
        }
        LOG.info("Registered " + this.commands.size() + " commands...");
    }

    private Call newCall(Route.CompiledRoute route, RequestBody body)
    {
        return this.modules.getHttpClient().newCall(newBuilder(route).method(route.getMethod().name(), body).build());
    }

    private Request.Builder newBuilder(Route.CompiledRoute route)
    {
        return new Request.Builder()
                .url(Requester.DISCORD_API_PREFIX + route.getCompiledRoute())
                .addHeader("Authorization", "Bot " + Config.INS.getToken());
    }

    public Map<String, Command> getCommands()
    {
        return this.commands;
    }

    public Command getCommand(String name)
    {
        return commands.get(name);
    }

    public void deleteAllCommands()
    {
        commands.clear();
    }

    @Override
    public void onDisable()
    {
        this.commands.clear();
    }
}