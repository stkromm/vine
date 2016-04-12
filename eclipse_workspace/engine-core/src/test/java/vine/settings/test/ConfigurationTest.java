package vine.settings.test;

import org.junit.Assert;
import org.junit.Test;

import vine.settings.Configuration;

public class ConfigurationTest {

    @Test
    public static void testConfigApply() {
        final Configuration configuration = new Configuration("res/settings.ini");
        configuration.apply();
        Assert.assertTrue(configuration.isApplied());
        configuration.addConfigurable(null);
        Assert.assertTrue(configuration.isApplied());
        configuration.addConfigurable(new TestConfig());
        Assert.assertTrue(!configuration.isApplied());
        configuration.apply();
        Assert.assertTrue(configuration.isApplied());
        configuration.putSetting("test", "");
        configuration.putSetting("", "");
        configuration.apply();
        configuration.apply();
        Assert.assertTrue(configuration.isApplied());
    }
}
