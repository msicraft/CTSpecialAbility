package me.msicraft.ctspecialability.SpecialAbility.Life;

import me.msicraft.ctcore.Utils.MathUtil;
import me.msicraft.ctspecialability.CTSpecialAbility;
import me.msicraft.ctspecialability.PlayerData.PlayerStats;
import me.msicraft.ctspecialability.SpecialAbility.Data.SpecialAbility;
import me.msicraft.ctspecialability.SpecialAbility.Data.SpecialAbilityType;
import me.msicraft.ctspecialability.SpecialAbility.Data.ToolCategory;
import me.msicraft.ctspecialability.SpecialAbility.Data.Trigger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExtraBoneMeal extends SpecialAbility {

    private final NamespacedKey KEY = new NamespacedKey(CTSpecialAbility.getPlugin(), "CTSpecialAbility_ExtraBoneMeal");

    public ExtraBoneMeal(String internalName) {
        super(Trigger.HARVEST, SpecialAbilityType.LIFE, internalName, Set.of(ToolCategory.ALL));
    }

    private double minChance = 0;
    private double maxChance = 0;
    private int minAmount = 0;
    private int maxAmount = 0;

    @Override
    public void updateVariables() {
        FileConfiguration config = CTSpecialAbility.getPlugin().getConfig();
        String path = "SpecialAbility.Life." + getInternalName();

        setEnabled(config.contains(path + ".Enabled") && config.getBoolean(path + ".Enabled"));
        setCoolDown(config.contains(path + ".CoolDown") ? config.getDouble(path + ".CoolDown") : 1);
        setDisplayName(config.contains(path + ".DisplayName") ? config.getString(path + ".DisplayName") : "Unknown");

        this.minChance = config.contains(path + ".MinChance") ? config.getDouble(path + ".MinChance") : 0;
        this.maxChance = config.contains(path + ".MaxChance") ? config.getDouble(path + ".MaxChance") : 0;

        this.minAmount = config.contains(path + ".MinAmount") ? config.getInt(path + ".MinAmount") : 0;
        this.maxAmount = config.contains(path + ".MaxAmount") ? config.getInt(path + ".MaxAmount") : 0;
    }

    @Override
    public void execute(BlockBreakEvent e, EquipmentSlot slot, PlayerStats playerStats) {
        super.execute(e, slot, playerStats);
        Ageable ageable = (Ageable) e.getBlock().getBlockData();
        if (ageable.getAge() == ageable.getMaximumAge()) {
            Player player = e.getPlayer();
            ItemStack itemStack = player.getInventory().getItem(slot);

            PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();
            if (dataContainer.has(KEY)) {
                if (!playerStats.isCoolDown(getInternalName())) {
                    String dataValue = dataContainer.get(KEY, PersistentDataType.STRING);
                    if (dataValue == null) {
                        return;
                    }
                    double value = Double.parseDouble(dataValue);

                    double max = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    double current = player.getHealth();
                    double cal = current + value;
                    if (cal > max) {
                        cal = max;
                    }

                    player.setHealth(cal);

                    playerStats.setCoolDown(getInternalName(), getCoolDown());
                }
            }
        }
    }

    @Override
    public void execute(PlayerHarvestBlockEvent e, EquipmentSlot slot, PlayerStats playerStats) {
        super.execute(e, slot, playerStats);
        Player player = e.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(slot);
        PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        if (dataContainer.has(KEY)) {
            if (!playerStats.isCoolDown(getInternalName())) {
                String dataValue = dataContainer.get(KEY, PersistentDataType.STRING);
                if (dataValue == null) {
                    return;
                }
                String[] data = dataValue.split(":");
                double chance = Double.parseDouble(data[0]);
                if (Math.random() <= chance) {
                    int amount = Integer.parseInt(data[1]);
                    player.getInventory().addItem(new ItemStack(Material.BONE_MEAL, amount));

                    playerStats.setCoolDown(getInternalName(), getCoolDown());
                }
            }
        }
    }

    @Override
    public void applySpecialAbilityToItemStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        double chance = Math.round(MathUtil.getRangeRandomDouble(maxChance, minChance) * 100) / 100.0;
        int value = MathUtil.getRangeRandomInt(maxAmount, minAmount);
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(SpecialAbility.KEY, PersistentDataType.STRING, getInternalName());
        dataContainer.set(KEY, PersistentDataType.STRING, chance + ":" + value);

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.GREEN + "특수능력 (" + getDisplayName() + ChatColor.GREEN
                + "): 농작물 수확시 " + ChatColor.AQUA + (chance*100) + "%" + ChatColor.GREEN + " 의 확률로 뼛가루 " + ChatColor.AQUA + value + ChatColor.GREEN + "개 추가 드랍"));
        if (itemMeta.hasLore()) {
            PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();
            List<Component> original = itemMeta.lore();
            for (Component c : original) {
                String s = plainTextComponentSerializer.serialize(c);
                if (s.contains(ChatColor.GREEN + "특수능력")) {
                    continue;
                }
                lore.add(c);
            }
        }
        itemMeta.lore(lore);

        itemStack.setItemMeta(itemMeta);
    }
}
