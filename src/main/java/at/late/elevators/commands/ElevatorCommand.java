package at.late.elevators.commands;

import at.late.elevators.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.List;
import java.util.Objects;

import static at.late.elevators.Main.elevatorCraft;
import static at.late.elevators.utils.FileManager.*;

public class ElevatorCommand implements CommandExecutor {
    public static boolean useSounds = storage.getBoolean("UseSounds");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = getPrefix();
        Player player = (Player) sender;
        if (sender instanceof Player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (args.length == 1) {
                        giveItem(player, 1, true, player);
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        try {
                            int amount = Integer.parseInt(args[1]);
                            giveItem(player, amount, true, player);
                            return true;
                        } catch (NumberFormatException nfe) {
                            giveItem(player, 1, true, player);
                            return true;
                        }
                    } else {
                        if (args.length >= 3) {
                            try {
                                int amount = Integer.parseInt(args[2]);
                                giveItem(player, amount, false, target);
                                return true;
                            } catch (NumberFormatException nfe) {
                                giveItem(player, 1, false, target);
                                return true;
                            }
                        } else {
                            giveItem(player, 1, false, target);
                            return true;
                        }
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    reloadConfig();
                    player.sendMessage(getPrefix() + "§aConfig has been reloaded!");
                    return true;
                } else if (args[0].equalsIgnoreCase("info")) {
                    Block block = player.getTargetBlock(null, 6);
                    if (block == null) {
                        player.sendMessage(prefix + storage.getString("NotLookingOnBlock").replaceAll("&", "§"));
                        return true;
                    }
                    if (block.getType() != Material.DAYLIGHT_DETECTOR) {
                        player.sendMessage(prefix + Objects.requireNonNull(storage.getString("NoElevator")).replaceAll("&", "§"));
                        playSoundFail(player);
                        return true;
                    }
                    TileState state = (TileState) block.getState();
                    PersistentDataContainer container = state.getPersistentDataContainer();
                    NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "elevator");
                    if (container.has(key, PersistentDataType.STRING)) {
                        player.sendMessage(prefix + Objects.requireNonNull(storage.getString("Elevator")).replaceAll("&", "§"));
                        playSoundSucces(player);
                        return true;
                    } else {
                        player.sendMessage(prefix + Objects.requireNonNull(storage.getString("NoElevator")).replaceAll("&", "§"));
                        playSoundFail(player);
                        return true;
                    }
                } else {
                    sendUsage(player);
                    playSoundFail(player);
                    return true;
                }
            }
             else{
                    sendUsage(player);
                    playSoundFail(player);
                    return true;
                }
        } else {
            sender.sendMessage(prefix + Objects.requireNonNull(storage.getString("MustBeAPlayer")).replaceAll("&", "§"));
            return true;
        }
    }
    public static void giveItem(Player player, int amount, boolean you, Player target) {
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
        itemStack.setItemMeta(itemMeta);
        itemStack.setAmount(amount);
        if (you) {
            if (amount == 1) {
                playSoundSucces(player);
                player.sendMessage(getPrefix() + Objects.requireNonNull(storage.getString("YouGotElevator")).replaceAll("&", "§"));
                player.getInventory().addItem(itemStack);
            } else {
                playSoundSucces(player);
                String msg = Objects.requireNonNull(storage.getString("YouGotElevators")).replaceAll("&", "§");
                msg = msg.replaceAll("%amount%", String.valueOf(amount));
                player.sendMessage(getPrefix() + msg);
                player.getInventory().addItem(itemStack);
            }
        } else {
            if (amount == 1) {
                playSoundSucces(player);
                playSoundSucces(target);
                String msg2 = Objects.requireNonNull(storage.getString("YouGotElevatorFrom")).replaceAll("&", "§");
                msg2 = msg2.replaceAll("%player%", player.getName());
                target.sendMessage(getPrefix() + msg2);
                target.getInventory().addItem(itemStack);
                String msg = Objects.requireNonNull(storage.getString("GaveElevatorTo")).replaceAll("&", "§");
                msg = msg.replaceAll("%player%", target.getName());
                player.sendMessage(getPrefix() + msg);
            } else {
                playSoundSucces(target);
                playSoundSucces(player);
                String msg1 = Objects.requireNonNull(storage.getString("YouGotElevatorsFrom")).replaceAll("%amount%", String.valueOf(amount));
                msg1 = msg1.replaceAll("%player%", player.getName());
                msg1 = msg1.replaceAll("&", "§");
                target.sendMessage(getPrefix() + msg1);
                target.getInventory().addItem(itemStack);
                String msg2 = Objects.requireNonNull(storage.getString("GaveElevatorsTo")).replaceAll("%amount%", String.valueOf(amount));
                msg2 = msg2.replaceAll("%player%", target.getName());
                msg2 = msg2.replaceAll("&", "§");
                player.sendMessage(getPrefix() + msg2);
            }
        }
    }

    public static void playSoundSucces(Player player) {
        if(useSounds) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.0f, 1.0f);
        }
    }
    public static void playSoundFail(Player player) {
        if(useSounds) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 20.0f, 1.0f);
        }
    }
    public static void playSoundLevelup(Player player) {
        if(useSounds) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3.0f, 1.0f);
        }
    }
    private void sendUsage(Player player) {
        player.sendMessage(getPrefix() + Objects.requireNonNull(storage.getString("Usage")).replaceAll("&", "§"));
    }
}