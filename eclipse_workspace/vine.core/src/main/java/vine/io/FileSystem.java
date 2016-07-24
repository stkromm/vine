package vine.io;

import java.io.File;

public class FileSystem
{
    File rootDirectory;

    public boolean mount(final String path)
    {
        final File file = new File(path);
        if (file.exists() && file.isDirectory())
        {
            this.rootDirectory = file;
            return true;
        }
        return false;
    }

}
