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

    public static boolean denyBlockItem = true;

    public static List<String> blackListedItems = new ArrayList<>();
    public static List<String> whiteListedItems = new ArrayList<>();

    public static List<String> blackNamespaces = new ArrayList<>();

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

        denyBlockItem = config.getBooleanOrCreate("deny_block_item", true);

        blackListedItems = (List<String>) config.getOrCreate("blacklist_items", new ArrayList<>());
        whiteListedItems = (List<String>) config.getOrCreate("whitelist_items", new ArrayList<>());
        blackNamespaces = (List<String>) config.getOrCreate("deny_namespaces", new ArrayList<>());

        if (blackListedItems.isEmpty() || whiteListedItems.isEmpty() || blackNamespaces.isEmpty())
            save();
    }

    public static File getFile() {
        return configFile;
    }

    public static void save() {
        config.save(getFile(), true);
    }
}
