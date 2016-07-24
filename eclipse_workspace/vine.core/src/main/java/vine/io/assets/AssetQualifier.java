package vine.io.assets;

public interface AssetQualifier
{
    default String getDisplayName()
    {
        return getShortName();
    }

    String getShortName();

    String getFileMarker();

    boolean isUsed();

    boolean isValidValue(String value);

    boolean isMoreSpecific(final AssetQualifier qualifier);

    default boolean isMatchFor(final AssetQualifier qualifier)
    {
        return equals(qualifier);
    }
}
