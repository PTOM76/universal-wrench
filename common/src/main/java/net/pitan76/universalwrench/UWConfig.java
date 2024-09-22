package net.pitan76.universalwrench;

import net.pitan76.easyapi.FileControl;
import net.pitan76.easyapi.config.JsonConfig;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UWConfig {
    public static File configFile = new File(PlatformUtil.getConfigFolderAsFile(), "universalwrench.json");

    public static JsonConfig config = new JsonConfig(configFile);


    public static List<String> DEFAULT_WRENCHES = new ArrayList<>();
    public static List<String> wrenches = DEFAULT_WRENCHES;

    public static boolean initialized = false;
    public static void init() {
        if (initialized) return;
        initialized = true;

        reload();

        save();
    }

    public static void reload() {
        if (FileControl.fileExists(getFile())) {
            config.load(getFile());
        }

        wrenches = (List<String>) config.getOrCreate("wrenches", DEFAULT_WRENCHES);

        if (wrenches == DEFAULT_WRENCHES)
            save();
    }

    public static File getFile() {
        return configFile;
    }

    public static void save() {
        config.save(getFile(), true);
    }

    static {
        wrenches.add("gtceu:iron_wrench");
    }
}
