package at.late.elevators;

import at.late.elevators.commands.ElevatorCommand;
import at.late.elevators.commands.ElevatorTabCompleter;
import at.late.elevators.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static at.late.elevators.utils.FileManager.*;

public final class Main extends JavaPlugin {
    public static boolean plotSquaredFound = false;
    @Override
    public void onEnable() {
        try {
            setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(storage.getBoolean("CraftingRecipe" + "." + "Enabled")) {
            Bukkit.addRecipe(elevatorCraft());
        }
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new OnBlockBreak(), this);
        manager.registerEvents(new OnBlockPlace(), this);
        manager.registerEvents(new OnJump(), this);
        manager.registerEvents(new OnSneak(), this);
        manager.registerEvents(new OnToggle(), this);
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        getCommand("elevators").setExecutor(new ElevatorCommand());
        getCommand("elevators").setTabCompleter(new ElevatorTabCompleter());
        console.sendMessage("");
        console.sendMessage(getPrefix() + "§aEpicElevators Plugin has been enabled!");
        console.sendMessage("");
        console.sendMessage("§6Author: §f_2late_");
        console.sendMessage("");
        console.sendMessage("§6Support Discord: §fhttps://discord.gg/Uk7MTCaCPp");
        console.sendMessage("");
        if (getServer().getPluginManager().getPlugin("PlotSquared") == null) {
            console.sendMessage(getPrefix() + "§c§cNo PlotSquared found! If you have PlotSquared installed, \nenable Force PlotSquared in the config.yml");
        } else {
            console.sendMessage(getPrefix() + "§a§lPlotSquared connected!");
            plotSquaredFound = true;
        }

    }

    @Override
    public void onDisable() {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage("");
        console.sendMessage(getPrefix() + "§aEpic Elevators Plugin has been disabled!");
        console.sendMessage("");
    }
    public static ShapedRecipe elevatorCraft() {
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
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "elevatorcraft");

        ShapedRecipe recipe = new ShapedRecipe(key, itemStack);
        String A = storage.getString("CraftingRecipe" + "." + "slot1");
        if(A.equalsIgnoreCase("AIR")) {
            A = " ";
        } else A = "A";
        String B = storage.getString("CraftingRecipe" + "." + "slot2");
        if(B.equalsIgnoreCase("AIR")) {
            B = " ";
        } else B = "B";
        String C = storage.getString("CraftingRecipe" + "." + "slot3");
        if(C.equalsIgnoreCase("AIR")) {
            C = " ";
        } else C = "C";
        String D = storage.getString("CraftingRecipe" + "." + "slot4");
        if(D.equalsIgnoreCase("AIR")) {
            D = " ";
        } else D = "D";
        String E = storage.getString("CraftingRecipe" + "." + "slot5");
        if(E.equalsIgnoreCase("AIR")) {
            E = " ";
        } else E = "E";
        String F = storage.getString("CraftingRecipe" + "." + "slot6");
        if(F.equalsIgnoreCase("AIR")) {
            F = " ";
        } else F = "F";
        String G = storage.getString("CraftingRecipe" + "." + "slot7");
        if(G.equalsIgnoreCase("AIR")) {
            G = " ";
        } else G = "G";
        String H = storage.getString("CraftingRecipe" + "." + "slot8");
        if(H.equalsIgnoreCase("AIR")) {
            H = " ";
        }else H = "H";
        String I = storage.getString("CraftingRecipe" + "." + "slot9");
        if(I.equalsIgnoreCase("AIR")) {
            I = " ";
        } else I = "I";
        recipe.shape(A + B + C, D + E + F, G + H + I) ;
        if(A != " ") {
            recipe.setIngredient('A', Material.valueOf(storage.getString("CraftingRecipe" + "." + "slot1")));
        }
        if(B != " ") {
            recipe.setIngredient('B', Material.valueOf(storage.getString("CraftingRecipe" + "." + "slot2")));
        }
        if(C != " ") {
            recipe.setIngredient('C', Material.valueOf(storage.getString("CraftingRecipe" + "." + "slot3")));
        }
        if(D != " ") {
            recipe.setIngredient('D', Material.valueOf(storage.getString("CraftingRecipe" + "." + "slot4")));
        }
        if(E != " ") {
            recipe.setIngredient('E', Material.valueOf(storage.getString("CraftingRecipe" + "." + "slot5")));
        }
        if(F != " ") {
            recipe.setIngredient('F', Material.valueOf(storage.getString("CraftingRecipe" + "." + "slot6")));
        }
        if(G != " ") {
            recipe.setIngredient('G', Material.valueOf(storage.getString("CraftingRecipe" + "." + "slot7")));
        }
        if(H != " ") {
            recipe.setIngredient('H', Material.valueOf(storage.getString("CraftingRecipe" + "." + "slot8")));
        }
        if(I != " ") {
            recipe.setIngredient('I', Material.valueOf(storage.getString("CraftingRecipe" + "." + "slot9")));
        }
        return recipe;
    }
}
