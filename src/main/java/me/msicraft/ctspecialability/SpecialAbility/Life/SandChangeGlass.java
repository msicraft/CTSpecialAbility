package me.msicraft.ctspecialability.SpecialAbility.Life;

import me.msicraft.ctspecialability.CTSpecialAbility;
import me.msicraft.ctspecialability.PlayerData.PlayerStats;
import me.msicraft.ctspecialability.SpecialAbility.Data.SpecialAbility;
import me.msicraft.ctspecialability.SpecialAbility.Data.SpecialAbilityType;
import me.msicraft.ctspecialability.SpecialAbility.Data.ToolCategory;
import me.msicraft.ctspecialability.SpecialAbility.Data.Trigger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SandChangeGlass extends SpecialAbility {

    public SandChangeGlass(String internalName) {
        super(new NamespacedKey(CTSpecialAbility.getPlugin(), "CTSpecialAbility_SandChangeGlass")
                ,Trigger.BLOCK_BREAK, SpecialAbilityType.LIFE, internalName, Set.of(ToolCategory.SHOVEL));
    }

    @Override
    public void updateVariables() {
        FileConfiguration config = CTSpecialAbility.getPlugin().getConfig();
        String path = "SpecialAbility.Life." + getInternalName();

        setEnabled(config.contains(path + ".Enabled") && config.getBoolean(path + ".Enabled"));
        setCoolDown(config.contains(path + ".CoolDown") ? config.getDouble(path + ".CoolDown") : 1);
        setDisplayName(config.contains(path + ".DisplayName") ? config.getString(path + ".DisplayName") : "Unknown");
    }

    @Override
    public void execute(BlockBreakEvent e, EquipmentSlot slot, PlayerStats playerStats) {
        super.execute(e, slot, playerStats);
        Block block = e.getBlock();
        if (block.getType() == Material.SAND) {
            Player player = e.getPlayer();
            ItemStack itemStack = player.getInventory().getItem(slot);
            PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();
            if (dataContainer.has(getKey())) {
                if (!playerStats.isCoolDown(getInternalName())) {
                    e.setCancelled(true);

                    World world = block.getWorld();
                    Location location = block.getLocation();

                    block.setType(Material.AIR);
                    world.dropItemNaturally(location, new ItemStack(Material.GLASS, 1));

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
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(SpecialAbility.KEY, PersistentDataType.STRING, getInternalName());
        dataContainer.set(getKey(), PersistentDataType.STRING, "SandChangeToGlass");

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.GREEN + "특수능력 (" + getDisplayName() + ChatColor.GREEN
                + "): 모래 블럭 채굴 시 유리 블럭으로 드랍"));
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
