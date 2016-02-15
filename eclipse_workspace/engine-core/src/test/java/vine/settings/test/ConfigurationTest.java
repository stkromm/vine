package vine.settings.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import vine.settings.Configuration;

public class ConfigurationTest {

    @Test
    public void testConfigApply() {
        Configuration configuration = new Configuration("res/settings.ini");
        configuration.apply();
        assertTrue(configuration.isApplied());
        configuration.addConfigurable(null);
        assertTrue(configuration.isApplied());
        configuration.addConfigurable(new TestConfig());
        assertTrue(!configuration.isApplied());
        configuration.apply();
        assertTrue(configuration.isApplied());
        configuration.putSetting("test", "");
        configuration.putSetting("", "");
        configuration.apply();
        configuration.apply();
        assertTrue(configuration.isApplied());
    }
}