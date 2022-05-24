package at.late.elevators.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static File file = new File("plugins//EpicElevators//config.yml");
    public static YamlConfiguration storage = YamlConfiguration.loadConfiguration(file);
    public static void setup() throws IOException {
        storage.options().copyDefaults(true);
        storage.addDefault("Prefix", "&5&lEpic&f&lElevators &7| &f");
        storage.addDefault("ForcePlotSquared", false);
        storage.addDefault("CraftingRecipe" + "." + "Enabled", true);
        storage.addDefault("CraftingRecipe" + "." + "slot1", "AIR");
        storage.addDefault("CraftingRecipe" + "." + "slot2", "EMERALD_BLOCK");
        storage.addDefault("CraftingRecipe" + "." + "slot3", "AIR");
        storage.addDefault("CraftingRecipe" + "." + "slot4", "DIAMOND_BLOCK");
        storage.addDefault("CraftingRecipe" + "." + "slot5", "NETHERITE_SCRAP");
        storage.addDefault("CraftingRecipe" + "." + "slot6", "DIAMOND_BLOCK");
        storage.addDefault("CraftingRecipe" + "." + "slot7", "AIR");
        storage.addDefault("CraftingRecipe" + "." + "slot8", "EMERALD_BLOCK");
        storage.addDefault("CraftingRecipe" + "." + "slot9", "AIR");
        List<String> allowedWorlds = new ArrayList<String>();
        allowedWorlds.add("plot");
        allowedWorlds.add("plotwelt");
        allowedWorlds.add("plotworld");
        allowedWorlds.add("plots");
        storage.addDefault("AllowedWorlds", allowedWorlds);
        storage.addDefault("NotOnPlot", "&cYou have to be on a Plot!");
        storage.addDefault("NotInWorld", "&cYou cannot do this in %world%!");
        storage.addDefault("MustBeAPlayer", "&cYou have to be a player!");
        storage.addDefault("NotLookingOnBlock", "&cYou have to be looking on a block!");
        storage.addDefault("NoElevator", "&cThis is not an elevator!");
        storage.addDefault("Elevator", "&aThis is an elevator!");
        storage.addDefault("ElevatorPrivate", "&cThis elevator is now private!");
        storage.addDefault("ElevatorPublic", "&aEveryone can now use this elevator!");
        storage.addDefault("TeleportLore", "&fFloor &6%current_floor%&7/&6%max_floors%");
        storage.addDefault("GroundFloor", "§fGround Floor");
        storage.addDefault("ItemName", "&f&k!&6&lElevator&f&k!");
        storage.addDefault("ItemGlow", true);
        storage.addDefault("UseNewHeightLimits", false);
        storage.addDefault("UseSounds", true);
        storage.addDefault("ElevatorPlaced", "&aElevator has been placed!");
        storage.addDefault("ElevatorDestroyed", "&cYou destroyed the elevator!");
        storage.addDefault("ElevatorPrivate", "&cThis elevator is private.");
        storage.addDefault("ElevatorTeleported", "&aYou have been teleported!");
        storage.addDefault("AllElevatorsPrivate", "&cAll elevators are private!");
        storage.addDefault("NoElevatorsFound", "&cNo Elevator was found.");
        storage.addDefault("NoElevatorsFound", "&cNo elevators where found!");
        storage.addDefault("Usage", "&cUsage: &7/elevator <give, player, amount> <info>, <reload>");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("&fPlace this elevator");
        lore.add("&fanywhere on your Plot!");
        storage.addDefault("ItemLore", lore);
        storage.addDefault("YouGotElevator", "&aYou have been given an elevator!");
        storage.addDefault("YouGotElevatorFrom", "&aYou just got an elevator from %player%!");
        storage.addDefault("YouGotElevators", "&aYou just got %amount% &aelevators!");
        storage.addDefault("YouGotElevatorsFrom", "&aYou just got %amount% elevators from %player%!");
        storage.addDefault("GaveElevatorTo", "&aYou just gave %player% an elevator!");
        storage.addDefault("YouGotElevators", "&aYou just got %amount% elevators from %player%!");
        storage.addDefault("GaveElevatorsTo", "&aYou just gave %amount% elevators to %player%!");
        saveConfig();
    }
    public static void reloadConfig() {
        storage = YamlConfiguration.loadConfiguration(file);
        saveConfig();
    }
    public static void saveConfig() {
        try {
            storage.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getPrefix() {
        return storage.getString("Prefix").replaceAll("&", "§");
    }
}
