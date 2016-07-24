package vine.graphics;

public enum ShaderUniforms
{
    VIEW_MATRIX("vw_matrix"), PROJECTION_MATRIX("pr_matrix");

    private final String key;

    private ShaderUniforms(final String key)
    {
        this.key = key;
    }

    @Override
    public String toString()
    {
        return this.key;
    }
}