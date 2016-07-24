package vine.window.test;

import java.io.File;

import org.junit.Test;

import vine.util.DataTable;

public class CsvTest
{

    @Test
    public void testDatatable()
    {
        final DataTable<Quest> quest = new DataTable<Quest>(Quest.class, new File("res/test-csv"));
        System.out.println(quest.getRecord(0).getDescription());
    }
}
