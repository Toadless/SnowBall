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
import net.toaddev.lavalite.entities.command.Command;
import net.toaddev.lavalite.entities.module.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandsModule extends Module
{
    private static final Logger LOG = LoggerFactory.getLogger(CommandsModule.class);
    private static final String COMMANDS_PACKAGE = "net.toaddev.lavalite.command";
    private Map<String, Command> commands;

    @Override
    public void onEnable()
    {
        scanCommands();
    }

    public void scanCommands()
    {
        LOG.info("Loading commands...");
        try(var result = new ClassGraph().acceptPackages(COMMANDS_PACKAGE).enableAnnotationInfo().scan())
        {
            this.commands = result.getSubclasses(Command.class.getName()).stream()
                    .map(ClassInfo::loadClass)
                    .filter(Command.class::isAssignableFrom)
                    .map(clazz ->
                    {
                        try
                        {
                            return (Command) clazz.getDeclaredConstructor().newInstance();
                        }
                        catch(Exception e)
                        {
                            LOG.info("Error while registering command: '{}'", clazz.getSimpleName(), e);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(Command::getName, Function.identity()));
        }
        LOG.info("Loaded {} commands", this.commands.size());

        new ArrayList<>(commands.entrySet()).forEach(stringCommandEntry ->
        {
            stringCommandEntry.getValue().getAlias().forEach(s -> {
                commands.put(s, commands.get(stringCommandEntry.getKey()));
            });
        });
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