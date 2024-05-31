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
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NerfVeinMining extends SpecialAbility {
    public NerfVeinMining(String internalName) {
        super(new NamespacedKey(CTSpecialAbility.getPlugin(), "CTSpecialAbility_NerfVeinMining"),
                Trigger.BLOCK_BREAK, SpecialAbilityType.LIFE, internalName, Set.of(ToolCategory.PICKAXE, ToolCategory.AXE, ToolCategory.SHOVEL, ToolCategory.HOE));
    }

    private int minAmount = 0;
    private int maxAmount = 0;
    private int radius = 1;

    @Override
    public void updateVariables() {
        FileConfiguration config = CTSpecialAbility.getPlugin().getConfig();
        String path = "SpecialAbility.Life." + getInternalName();

        setEnabled(config.contains(path + ".Enabled") && config.getBoolean(path + ".Enabled"));
        setCoolDown(config.contains(path + ".CoolDown") ? config.getDouble(path + ".CoolDown") : 1);
        setDisplayName(config.contains(path + ".DisplayName") ? config.getString(path + ".DisplayName") : "Unknown");

        this.minAmount = config.contains(path + ".MinAmount") ? config.getInt(path + ".MinAmount") : 0;
        this.maxAmount = config.contains(path + ".MaxAmount") ? config.getInt(path + ".MaxAmount") : 0;

        this.radius = config.contains(path + ".Radius")? config.getInt(path + ".Radius") : 1;
    }

    @Override
    public void execute(BlockBreakEvent e, EquipmentSlot slot, PlayerStats playerStats) {
        super.execute(e, slot, playerStats);
        Player player = e.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(slot);
        PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        if (dataContainer.has(getKey())) {
            if (!playerStats.isCoolDown(getInternalName())) {
                String dataValue = dataContainer.get(getKey(), PersistentDataType.STRING);
                if (dataValue == null) {
                    return;
                }
                int amount = Integer.parseInt(dataValue);

                List<Block> list = new ArrayList<>();
                Block baseBlock = e.getBlock();
                World world = baseBlock.getWorld();
                Location baseLocation = baseBlock.getLocation();
                int baseX = baseLocation.getBlockX();
                int baseY = baseLocation.getBlockY();
                int baseZ = baseLocation.getBlockZ();
                int count = 0;
                topLabel:
                for (int x = (baseX - radius); x < (baseX + radius); x++) {
                    for (int y = (baseY - radius); y < (baseY + radius); y++) {
                        for (int z = (baseZ - radius); z < (baseZ + radius); z++) {
                            if (count >= amount) {
                                break topLabel;
                            }
                            Block block = world.getBlockAt(x, y, z);
                            if (x == baseX && y == baseY && z == baseZ) {
                                continue;
                            }
                            if (block.getType() == baseBlock.getType()) {
                                list.add(block);
                                count++;
                            }
                        }
                    }
                }
                for (Block block : list) {
                    block.breakNaturally(itemStack);
                }

                playerStats.setCoolDown(getInternalName(), getCoolDown());
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
        int value = MathUtil.getRangeRandomInt(maxAmount, minAmount);
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(SpecialAbility.KEY, PersistentDataType.STRING, getInternalName());
        dataContainer.set(getKey(), PersistentDataType.STRING, String.valueOf(value));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.GREEN + "특수능력 (" + getDisplayName() + ChatColor.GREEN
                + "): 블럭을 부술때 근처 같은 형식의 블럭 " + ChatColor.AQUA + value + ChatColor.GREEN + "개 추가 채굴"));
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
