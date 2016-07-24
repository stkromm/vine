package vine.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import vine.util.reflection.ReflectUtils;

public class DataTable<T extends DataRecord>
{
    private final String[] header;
    private final List<T>  records = new ArrayList<>();

    public DataTable(final Class<T> clazz, final File file)
    {
        List<CSVRecord> csvRecords = new ArrayList<>();
        try (CSVParser p = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.RFC4180))
        {
            csvRecords = p.getRecords();
        } catch (final IOException exception)
        {
            Log.exception("Failed to load csv datatable.", exception);
        }

        header = new String[csvRecords.size() > 0 ? csvRecords.get(0).size() : 0];
        if (csvRecords.size() > 0)
        {
            final CSVRecord rec = csvRecords.remove(0);
            for (int i = 0; i < rec.size(); i++)
            {
                header[i] = rec.get(i);
            }
        }

        final Map<String, Field> fields = new HashMap<>();
        for (final Field field : clazz.getDeclaredFields())
        {
            final DataColumn[] a = field.getAnnotationsByType(DataColumn.class);
            if (a != null && a.length != 0)
            {
                fields.put(a[0].name(), field);
            }
        }

        for (final CSVRecord record : csvRecords)
        {
            T instance = null;
            try
            {
                instance = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException exception)
            {
                Log.exception(
                        "Can't create Datatable with type " + clazz
                                + ". Datatable Entry types must define a parameterless constructor.",
                        exception);
            }

            for (int i = 0; i < record.size(); i++)
            {
                final String name = header[i];
                final Field field = fields.get(name);

                // The type T has no field for the column name (i'th column).
                if (field == null)
                {
                    continue;
                }

                if (!ReflectUtils
                        .setField(clazz, field, ReflectUtils.toObject(field.getType(), record.get(i)), instance))
                {
                    Log.debug(
                            "Failed to set the field + %s. Fields that are used to fill with datatable data must define standard setter functions.",
                            field);
                }
                this.records.add(instance);
            }
        }
    }

    public int getSize()
    {
        return records.size();

    }

    public T getRecord(final int index)
    {
        return records.get(index);
    }

    public T findRecord(final String value)
    {
        for (final String entry : header)
        {
            return findRecord(entry, value);
        }
        return null;
    }

    public T findRecord(final String column, final Object value)
    {
        final int columnIndex = Arrays.binarySearch(header, column);
        for (final T record : records)
        {
            if (record.get(columnIndex).equals(value))
            {
                return record;
            }
        }
        return null;
    }
}
