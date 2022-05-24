package at.late.elevators.listeners;

import at.late.elevators.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;

import static at.late.elevators.utils.FileManager.getPrefix;
import static at.late.elevators.utils.FileManager.storage;

public class OnBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if(block.getType() == Material.DAYLIGHT_DETECTOR) {
            Player player = event.getPlayer();
            TileState stateTarget = (TileState) block.getState();
            PersistentDataContainer containerTarget = stateTarget.getPersistentDataContainer();
            NamespacedKey keyTarget = new NamespacedKey(Main.getPlugin(Main.class), "elevator");
            if (containerTarget.has(keyTarget, PersistentDataType.STRING)) {
                if(player.getGameMode() == GameMode.CREATIVE) {
                    return;
                }
                if(event.isCancelled()) {
                    return;
                }
                ItemStack itemStack = new ItemStack(Material.DAYLIGHT_DETECTOR);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(Objects.requireNonNull(storage.getString("ItemName")).replaceAll("&", "§"));
                itemMeta.setLocalizedName("elevator");
                if(storage.getBoolean("ItemGlow")) {
                    itemMeta.addEnchant(Enchantment.SILK_TOUCH, 1, true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                if(storage.getList("ItemLore") != null) {
                    List<String> lore = storage.getStringList("ItemLore");
                    for(int i = 0; i < lore.size() ; i++) {
                        lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
                    }
                    itemMeta.setLore(lore);
                }
                player.sendMessage(getPrefix() + storage.getString("ElevatorDestroyed").replaceAll("&", "§"));
                itemStack.setItemMeta(itemMeta);
                event.setDropItems(false);
                block.getLocation().getWorld().dropItemNaturally(block.getLocation(), itemStack);
            }
        }
    }
}
