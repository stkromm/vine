package vine.window.test;

import vine.util.DataColumn;
import vine.util.DataRecord;

public class Quest implements DataRecord
{
    @DataColumn(name = "Name")
    private String name;
    @DataColumn(name = "Text")
    private String description;

    public Quest()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

}
