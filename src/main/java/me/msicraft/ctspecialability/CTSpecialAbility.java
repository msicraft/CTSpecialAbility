package me.msicraft.ctspecialability;

import me.msicraft.ctspecialability.Command.MainCommand;
import me.msicraft.ctspecialability.PlayerData.Event.PlayerRelatedEvent;
import me.msicraft.ctspecialability.PlayerData.PlayerStatsManager;
import me.msicraft.ctspecialability.SpecialAbility.Event.SpecialAbilityApplyEvent;
import me.msicraft.ctspecialability.SpecialAbility.Event.SpecialAbilityRegisterEvent;
import me.msicraft.ctspecialability.SpecialAbility.SpecialAbilityManager;
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

    private PlayerStatsManager playerStatsManager;
    private SpecialAbilityManager specialAbilityManager;

    @Override
    public void onEnable() {
        plugin = this;
        createConfigFiles();

        playerStatsManager = new PlayerStatsManager(this);
        specialAbilityManager = new SpecialAbilityManager(this);

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
        getServer().getPluginManager().registerEvents(new PlayerRelatedEvent(this), this);
        getServer().getPluginManager().registerEvents(new SpecialAbilityRegisterEvent(this), this);
        getServer().getPluginManager().registerEvents(new SpecialAbilityApplyEvent(this), this);
    }

    private void commandRegister() {
        getCommand("ctspecialability").setExecutor(new MainCommand(this));
    }

    public void reloadVariables() {
        reloadConfig();

        specialAbilityManager.reloadVariables();
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

    public PlayerStatsManager getPlayerStatsManager() {
        return playerStatsManager;
    }

    public SpecialAbilityManager getSpecialAbilityManager() {
        return specialAbilityManager;
    }

}
