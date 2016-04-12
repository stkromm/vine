package vine.assets;

import java.util.HashMap;
import java.util.Map;

public class AssetTable {
    Map<String, AssetPointer> pointer;

    public AssetTable(String pointerListPath) {
        this.pointer = new HashMap<>();
        this.pointer.put("frag", new AssetPointer("res/test/frag.shader", 0, 0));
        this.pointer.put("hero", new AssetPointer("res/test/hero.png", 0, 0));
        this.pointer.put("chipset", new AssetPointer("res/test/chipset.png", 0, 0));
        this.pointer.put("font", new AssetPointer("res/font.ttf", 0, 0));
        this.pointer.put("herosheet", new AssetPointer("res/test/HeroSheet.png", 0, 0));
    }
}
