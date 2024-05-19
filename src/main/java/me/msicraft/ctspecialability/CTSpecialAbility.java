package me.msicraft.ctspecialability;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class CTSpecialAbility extends JavaPlugin {

    private static CTSpecialAbility plugin;

    public static CTSpecialAbility getPlugin() {
        return plugin;
    }

    public static final String PREFIX = ChatColor.GREEN + "[CTSpecialAbility]";

    @Override
    public void onEnable() {
        plugin = this;
        createConfigFiles();

        eventRegister();
        commandRegister();
        reloadVariables();

        getServer().getConsoleSender().sendMessage(PREFIX + " 플러그인이 활성화 되었습니다");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(PREFIX + ChatColor.RED + " 플러그인이 비활성화 되었습니다");
    }

    private void eventRegister() {
    }

    private void commandRegister() {
    }

    public void reloadVariables() {
        reloadConfig();
    }

    private void createConfigFiles() {
        File configf = new File(getDataFolder(), "config.yml");
        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
