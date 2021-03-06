package net.toaddev.snowball.objects.command;

import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CommandOption<T> implements CommandOptionsHolder
{
    private final CommandOptionType type;
    private final String name, description;
    private final List<CommandOption<?>> options;
    private boolean isDefault, isRequired;

    protected CommandOption(CommandOptionType type, String name, String description)
    {
        this.type = type;
        this.name = name.toLowerCase();
        this.description = description;
        this.isDefault = false;
        this.isRequired = false;
        this.options = new ArrayList<>();
    }

    public static DataArray toJSON(Collection<CommandOption<?>> options)
    {
        return DataArray.fromCollection(
                options.stream().map(CommandOption::toJSON).collect(Collectors.toList())
        );
    }

    public abstract T parseValue(Object value);

    public CommandOption<?> addOptions(CommandOption<?>... options)
    {
        this.options.addAll(List.of(options));
        return this;
    }

    public CommandOption<?> setDefault()
    {
        this.isDefault = true;
        return this;
    }

    public CommandOption<?> required()
    {
        this.isRequired = true;
        return this;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public CommandOptionType getType()
    {
        return this.type;
    }

    public boolean isDefault()
    {
        return this.isDefault;
    }

    public boolean isRequired()
    {
        return this.isRequired;
    }

    @Override
    public List<CommandOption<?>> getOptions()
    {
        return options;
    }

    public DataObject toJSON()
    {
        var json = DataObject.empty()
                .put("type", this.type.getType())
                .put("name", this.name)
                .put("description", this.description);
        if (this.isDefault)
        {
            json.put("default", true);
        }
        if (this.isRequired)
        {
            json.put("required", true);
        }
        if (!this.options.isEmpty())
        {
            json.put("options", CommandOption.toJSON(this.options));
        }
        return json;
    }
}