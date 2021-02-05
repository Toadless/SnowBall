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

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.toaddev.lavalite.entities.command.Command;
import net.toaddev.lavalite.entities.module.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;

public class CommandsModule extends Module
{
    private static final Logger LOG = LoggerFactory.getLogger(CommandsModule.class);
    private static final String COMMANDS_PACKAGE = "net.toaddev.lavalite.command";
    private ClassGraph classGraph;
    private Map<String, Command> commands;

    @Override
    public void onEnable()
    {
        this.classGraph = new ClassGraph().acceptPackages(COMMANDS_PACKAGE);
        this.commands = new HashMap<>();

        scanCommands();
    }

    public void scanCommands()
    {
        LOG.info("Loading commands...");

        try(ScanResult result = classGraph.scan())
        {
            for(ClassInfo clazz : result.getAllClasses())
            {
                Constructor<?>[] constructors = clazz.loadClass().getDeclaredConstructors();
                if(constructors.length == 0)
                {
                    LOG.warn("No constructor found for Command class (" + clazz.getSimpleName() + ")!");
                    continue;
                }
                if(constructors[0].getParameterCount() > 0)
                {
                    continue;
                }
                Object instance = constructors[0].newInstance();
                if(!(instance instanceof Command))
                {
                    LOG.warn("Non Command class (" + clazz.getSimpleName() + ") found in commands package!");
                    continue;
                }
                Command cmd = (Command) instance;
                commands.put(cmd.getName(), cmd);
                for(String alias : cmd.getAliases()) commands.put(alias, cmd);
            }

            LOG.info("Loaded {} commands", this.commands.size());
        }
        catch(Exception exception)
        {
            LOG.error("A exception occurred whilst loading commands. Exception: ", exception);
        }
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