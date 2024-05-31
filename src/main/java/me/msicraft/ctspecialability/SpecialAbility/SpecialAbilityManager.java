package me.msicraft.ctspecialability.SpecialAbility;

import me.msicraft.ctspecialability.CTSpecialAbility;
import me.msicraft.ctspecialability.SpecialAbility.Combat.ExtraFlatDamage;
import me.msicraft.ctspecialability.SpecialAbility.Combat.FlatLifeSteal;
import me.msicraft.ctspecialability.SpecialAbility.Combat.MaxHealthBasedLifeSteal;
import me.msicraft.ctspecialability.SpecialAbility.Combat.TargetHealthBasedExtraDamage;
import me.msicraft.ctspecialability.SpecialAbility.Data.SpecialAbility;
import me.msicraft.ctspecialability.SpecialAbility.Data.SpecialAbilityType;
import me.msicraft.ctspecialability.SpecialAbility.Life.ExtraBoneMeal;
import me.msicraft.ctspecialability.SpecialAbility.Life.NerfVeinMining;
import me.msicraft.ctspecialability.SpecialAbility.Life.SandChangeGlass;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpecialAbilityManager {

    private final CTSpecialAbility plugin;

    private final Map<String, SpecialAbility> map = new HashMap<>();

    public SpecialAbilityManager(CTSpecialAbility plugin) {
        this.plugin = plugin;

        map.put("FlatLifeSteal", new FlatLifeSteal("FlatLifeSteal"));
        map.put("ExtraFlatDamage", new ExtraFlatDamage("ExtraFlatDamage"));
        map.put("MaxHealthBasedLifeSteal", new MaxHealthBasedLifeSteal("MaxHealthBasedLifeSteal"));
        map.put("TargetHealthBasedExtraDamage", new TargetHealthBasedExtraDamage("TargetHealthBasedExtraDamage"));

        map.put("ExtraBoneMeal", new ExtraBoneMeal("ExtraBoneMeal"));
        map.put("SandChangeGlass", new SandChangeGlass("SandChangeGlass"));
        map.put("NerfVeinMining", new NerfVeinMining("NerfVeinMining"));
    }

    public void reloadVariables() {
        ConfigurationSection combatSection = plugin.getConfig().getConfigurationSection("SpecialAbility.Combat");
        if (combatSection != null) {
            Set<String> sets = combatSection.getKeys(false);
            for (String key : sets) {
                SpecialAbility specialAbility = map.get(key);
                if (specialAbility == null) {
                    continue;
                }
                specialAbility.updateVariables();
            }
        }

        ConfigurationSection lifeSection = plugin.getConfig().getConfigurationSection("SpecialAbility.Life");
        if (lifeSection != null) {
            Set<String> sets = lifeSection.getKeys(false);
            for (String key : sets) {
                SpecialAbility specialAbility = map.get(key);
                if (specialAbility == null) {
                    continue;
                }
                specialAbility.updateVariables();
            }
        }
    }

    public Set<String> getInternalNames() {
        return map.keySet();
    }

    public SpecialAbility getSpecialAbility(String internalName) {
        return map.get(internalName);
    }

    public Set<SpecialAbility> getSpecialAbilities(SpecialAbilityType specialAbilityType) {
        Set<SpecialAbility> sets = new HashSet<>();
        for (String internalName : map.keySet()) {
            SpecialAbility specialAbility = map.get(internalName);
            if (specialAbility.getSpecialAbilityType() == specialAbilityType) {
                sets.add(specialAbility);
            }
        }
        return sets;
    }

}
