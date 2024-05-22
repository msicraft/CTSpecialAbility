package me.msicraft.ctspecialability.SpecialAbility;

import me.msicraft.ctspecialability.CTSpecialAbility;
import me.msicraft.ctspecialability.SpecialAbility.Data.SpecialAbility;
import me.msicraft.ctspecialability.SpecialAbility.Data.SpecialAbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecialAbilityManager {

    private final CTSpecialAbility plugin;

    public SpecialAbilityManager(CTSpecialAbility plugin) {
        this.plugin = plugin;
    }

    public void reloadVariables() {
    }

}
