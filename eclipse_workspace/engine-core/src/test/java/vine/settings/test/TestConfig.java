package vine.settings.test;

import java.util.List;

import vine.settings.Configurable;

public class TestConfig extends Configurable {
    public TestConfig() {
        this.properties.put("test", new SettingHandler() {
            String val;

            @Override
            public List<String> getOptions() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void apply(String value) {
                val = value;
            }

            @Override
            public String getKey() {
                return "test";
            }

            @Override
            public String getCurrentValue() {
                return val;
            }
        });
    }
}
